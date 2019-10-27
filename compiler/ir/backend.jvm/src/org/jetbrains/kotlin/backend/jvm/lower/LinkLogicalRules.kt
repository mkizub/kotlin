/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.jvm.lower

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.descriptors.synthesizedName
import org.jetbrains.kotlin.backend.common.ir.createImplicitParameterDeclarationWithWrappedDescriptor
import org.jetbrains.kotlin.backend.common.ir.isStatic
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irThrow
import org.jetbrains.kotlin.backend.common.lower.parents
import org.jetbrains.kotlin.backend.common.phaser.makeIrFilePhase
import org.jetbrains.kotlin.backend.jvm.JvmBackendContext
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.LocalVariableDescriptor
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrVariableImpl
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrRuleBody
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.linkLogicalRules
import org.jetbrains.kotlin.ir.symbols.IrVariableSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.isRule
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.load.java.JavaVisibilities
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffsetSkippingComments
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance

const val RULE_BACKTRACK_FIELD_NAME = "bt$"

val linkLogicalRulesPhase = makeIrFilePhase(
    ::LinkLogicalRulesLowering,
    "LinkLogicalRules",
    "Build state machine for logical rules and add continuation classes to rule methods"
)

private object LOGICAL_RULES_CLASS : IrDeclarationOriginImpl("LOGICAL_RULES_CLASS")

/**
 * from
 *   rule fun foo(args) { rules }
 * generate
 *   class foo$frame : RuleFrame {
 *     fun calculate(): Boolean = foo$body(this)
 *   }
 *   fun foo$body(frame$$: foo$frame): Boolean { rules }
 *   fun foo(args): Rule = foo$frame(args)
 */

private class LinkLogicalRulesLowering(private val context: JvmBackendContext) :
    FileLoweringPass {
    private val ruleInterface by lazy {
        context.getTopLevelClass(DescriptorUtils.LOGICAL_RULES_PACKAGE_FQ_NAME.child(Name.identifier("Rule")))
    }
    private val ruleBaseImpl by lazy {
        context.getTopLevelClass(DescriptorUtils.LOGICAL_RULES_PACKAGE_FQ_NAME.child(Name.identifier("RuleFrame")))
    }

    override fun lower(irFile: IrFile) {
        val ruleFunctions = collectRuleFunctions(irFile)
        for (ruleFunc in ruleFunctions) {
            linkLogicalRulesForFunction(ruleFunc)
            val ownerClass = ruleFunc.parents.firstIsInstance<IrClass>()
            val frameClass = ruleBaseImpl.owner.generateImplClassForFunction(ownerClass, ruleFunc)
            val bodyFunc = ownerClass.generateImplFunctionForRule(ruleFunc, frameClass)
            frameClass.addCalculateFunction(bodyFunc)
        }
    }

    private fun collectRuleFunctions(irElement: IrElement): List<IrFunction> {
        val ruleFunctions = arrayListOf<IrFunction>()
        irElement.acceptChildrenVoid(object : IrElementVisitorVoid {
            override fun visitElement(element: IrElement) {
                element.acceptChildrenVoid(this)
            }

            override fun visitFunction(declaration: IrFunction) {
                if (declaration.isRule)
                    ruleFunctions += declaration
            }
        })
        return ruleFunctions
    }

    private fun linkLogicalRulesForFunction(ruleFunction: IrFunction) {
        val body = ruleFunction.body
        if (body is IrRuleBody)
            body.linkLogicalRules()
    }

    private fun IrClass.generateImplFunctionForRule(ruleFunc: IrFunction, irClass: IrClass): IrFunction {
        val bodyFunc = addFunction(
            name = "${ruleFunc.name}\$body",
            returnType = context.irBuiltIns.booleanType,
            modality = Modality.FINAL,
            isStatic = ruleFunc.isStatic,
            isSuspend = false
        )
        bodyFunc.addValueParameter("frame\$\$", irClass.defaultType)
        generateRuleBody(bodyFunc, ruleFunc)
        ruleFunc.body = context.createIrBuilder(ruleFunc.symbol).irBlockBody {
            +irReturn(
                irCall(irClass.symbol.constructors.single(), irClass.defaultType).apply {
                    ruleFunc.valueParameters.withIndex().forEach {
                        putValueArgument(it.index, irGet(it.value))
                    }
                }
            )
        }
        return bodyFunc
    }

    private fun IrClass.generateImplClassForFunction(ownerClass: IrClass, ruleFunc: IrFunction): IrClass {
        return createRuleClassFor(ownerClass, ruleFunc).apply {
            val capturedThisField = ruleFunc.dispatchReceiverParameter?.let { addField("this\$0", it.type) }
            val backtrackField = addField(RULE_BACKTRACK_FIELD_NAME, context.irBuiltIns.intType, JavaVisibilities.PACKAGE_VISIBILITY);
            val body = ruleFunc.body
            if (body is IrRuleBody) {
                for (i in 0..body.states)
                    addField(RULE_BACKTRACK_FIELD_NAME + i, context.irBuiltIns.intType, JavaVisibilities.PACKAGE_VISIBILITY)
            }
            addPrimaryConstructor(ruleFunc, backtrackField, capturedThisField)
        }
    }

    private fun IrClass.createRuleClassFor(ownerClass: IrClass, ruleFunc: IrFunction): IrClass = buildClass {
        name = "${ruleFunc.name}\$frame".synthesizedName
        origin = LOGICAL_RULES_CLASS
        visibility = JavaVisibilities.PACKAGE_VISIBILITY
    }.also { irClass ->
        irClass.createImplicitParameterDeclarationWithWrappedDescriptor()
        irClass.superTypes.add(defaultType)

        irClass.parent = ownerClass
        ownerClass.declarations.add(irClass)
    }

    private fun IrClass.addPrimaryConstructor(ruleFunc: IrFunction, backtrackField: IrField, capturedThisField: IrField?): IrConstructor =
        addConstructor {
            isPrimary = true
            returnType = defaultType
        }.also { constructor ->
            val capturedThisParameter = capturedThisField?.let { constructor.addValueParameter(it.name.asString(), it.type) }
            val args = ruleFunc.valueParameters.map {
                val field = addField(it.name.asString(), it.type)
                val param = constructor.addValueParameter(it.name.asString(), it.type)
                return@map Pair(field, param)
            }
            val superClassConstructor = ruleBaseImpl.owner.constructors.single {
                it.valueParameters.size == 0
            }
            constructor.body = context.createIrBuilder(constructor.symbol).irBlockBody {
                if (capturedThisField != null) {
                    +irSetField(irGet(thisReceiver!!), capturedThisField, irGet(capturedThisParameter!!))
                }
                +irDelegatingConstructorCall(superClassConstructor)
                for (arg in args) {
                    +irSetField(irGet(thisReceiver!!), arg.first, irGet(arg.second))
                }
                +irSetField(irGet(thisReceiver!!), backtrackField, irInt(-1))
            }
        }

    private fun IrClass.addCalculateFunction(bodyFunc: IrFunction): IrFunction {
        val function = addFunction(
            name = "calculate",
            returnType = context.irBuiltIns.booleanType
        )
        function.body = context.createIrBuilder(function.symbol).irBlockBody {
            +irReturn(
                irCall(bodyFunc.symbol, context.irBuiltIns.booleanType).apply {
                    putValueArgument(0, irGet(function.dispatchReceiverParameter!!))
                }
            )
        }
        return function
    }

    private fun generateRuleBody(bodyFunc: IrFunction, ruleFunc: IrFunction) {
        val body = ruleFunc.body
        ruleFunc.body = null

        val btDescriptor = LocalVariableDescriptor(
            bodyFunc.descriptor,
            Annotations.EMPTY,
            Name.identifier("bt$"),
            null,
            true, false,
            SourceElement.NO_SOURCE
        );

        val type: IrType;
        bodyFunc.body = context.createIrBuilder(bodyFunc.symbol).irBlockBody {
            +IrVariableImpl(
                UNDEFINED_OFFSET,
                UNDEFINED_OFFSET,
                LOGICAL_RULES_CLASS,
                btDescriptor,
                context.irBuiltIns.intType
            )
            if (body is IrRuleBody) {
                +body
            }
            +irReturnFalse()
        }

    }
}

/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.jvm.lower

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.descriptors.synthesizedName
import org.jetbrains.kotlin.backend.common.ir.createImplicitParameterDeclarationWithWrappedDescriptor
import org.jetbrains.kotlin.backend.common.ir.isStatic
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.parents
import org.jetbrains.kotlin.backend.common.phaser.makeIrFilePhase
import org.jetbrains.kotlin.backend.jvm.JvmBackendContext
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
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.linkLogicalRules
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.isRule
import org.jetbrains.kotlin.ir.visitors.*
import org.jetbrains.kotlin.load.java.JavaVisibilities
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance

const val RULE_BACKTRACK_FIELD_NAME = "bt$"

val logicalRulesPhase = makeIrFilePhase(
    ::LinkLogicalRulesLowering,
    "LogicalRules",
    "Build state machine for logical rules, add synthetic classes and methods"
)

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

    private val ruleBaseImpl by lazy {
        context.getTopLevelClass(DescriptorUtils.LOGICAL_RULES_PACKAGE_FQ_NAME.child(Name.identifier("RuleFrame")))
    }

    override fun lower(irFile: IrFile) {
        val ruleFunctions = collectRuleFunctions(irFile)
        for (ruleFunc in ruleFunctions)
            generateImplFunctionForRule(ruleFunc)
    }

    private fun collectRuleFunctions(irElement: IrElement): List<IrFunction> {
        val ruleFunctions = arrayListOf<IrFunction>()
        irElement.acceptChildrenVoid(object : IrElementVisitorVoid {
            override fun visitElement(element: IrElement) {
                element.acceptChildrenVoid(this)
            }

            override fun visitFunction(declaration: IrFunction) {
                if (declaration.isRule && declaration.body is IrRuleBody)
                    ruleFunctions += declaration
            }
        })
        return ruleFunctions
    }

    private fun generateImplFunctionForRule(ruleFunc: IrFunction): IrFunction {
        val irRuleBody: IrRuleBody = ruleFunc.body as IrRuleBody
        irRuleBody.linkLogicalRules()

        val ownerClass = ruleFunc.parents.firstIsInstance<IrClass>()
        val frameClass = buildClass {
            name = "${ruleFunc.name}\$frame".synthesizedName
            origin = IrDeclarationOrigin.LOGICAL_RULES_CLASS
            visibility = JavaVisibilities.PACKAGE_VISIBILITY
        }.also { irClass ->
            irClass.createImplicitParameterDeclarationWithWrappedDescriptor()
            irClass.superTypes.add(ruleBaseImpl.owner.defaultType)
            irClass.parent = ownerClass
            ownerClass.declarations.add(irClass)
        }.apply {
            val capturedThisField = ruleFunc.dispatchReceiverParameter?.let { addField("this\$0", it.type) }
            val backtrackField = addField(RULE_BACKTRACK_FIELD_NAME, context.irBuiltIns.intType, JavaVisibilities.PACKAGE_VISIBILITY)
            for (i in 0..irRuleBody.states)
                addField(RULE_BACKTRACK_FIELD_NAME + i, context.irBuiltIns.intType, JavaVisibilities.PACKAGE_VISIBILITY)
            addPrimaryConstructor(ruleFunc, backtrackField, capturedThisField)
        }
        ruleFunc.body = context.createIrBuilder(ruleFunc.symbol).irBlockBody {
            +irReturn(
                irCall(frameClass.symbol.constructors.single(), frameClass.defaultType).apply {
                    ruleFunc.valueParameters.withIndex().forEach {
                        putValueArgument(it.index, irGet(it.value))
                    }
                }
            )
        }

        val bodyFunc = ownerClass.addFunction(
            name = "${ruleFunc.name}\$body",
            returnType = context.irBuiltIns.booleanType,
            modality = Modality.FINAL,
            isStatic = ruleFunc.isStatic,
            isSuspend = false
        )
        bodyFunc.origin = IrDeclarationOrigin.LOGICAL_RULES_BODY
        val frameParam = bodyFunc.addValueParameter("frame\$\$", frameClass.defaultType)
        frameClass.addCalculateFunction(bodyFunc)

        generateRuleBody(bodyFunc, irRuleBody)

        addIteratorFields(irRuleBody, frameClass)
        rewriteLocalVariables(ruleFunc, bodyFunc, frameParam, frameClass)

        return bodyFunc
    }

    private fun IrClass.addPrimaryConstructor(ruleFunc: IrFunction, backtrackField: IrField, capturedThisField: IrField?): IrConstructor =
        addConstructor {
            isPrimary = true
            returnType = defaultType
        }.also { constructor ->
            val capturedThisParameter = capturedThisField?.let { constructor.addValueParameter(it.name.asString(), it.type) }
            val args = ruleFunc.valueParameters.map {
                val field = addField {
                    name = it.name
                    type = it.type
                    visibility = JavaVisibilities.PACKAGE_VISIBILITY
                    isFinal = true
                }
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

    private fun generateRuleBody(bodyFunc: IrFunction, irRuleBody: IrRuleBody) {
        val btDescriptor = LocalVariableDescriptor(
            bodyFunc.descriptor,
            Annotations.EMPTY,
            Name.identifier("bt\$"),
            null,
            true, false,
            SourceElement.NO_SOURCE
        )
        val wasDescriptor = LocalVariableDescriptor(
            bodyFunc.descriptor,
            Annotations.EMPTY,
            Name.identifier("was\$bound"),
            null,
            true, false,
            SourceElement.NO_SOURCE
        )

        bodyFunc.body = context.createIrBuilder(bodyFunc.symbol).irBlockBody {
            +IrVariableImpl(
                UNDEFINED_OFFSET,
                UNDEFINED_OFFSET,
                IrDeclarationOrigin.LOGICAL_RULES_BODY,
                btDescriptor,
                context.irBuiltIns.intType
            )
            +IrVariableImpl(
                UNDEFINED_OFFSET,
                UNDEFINED_OFFSET,
                IrDeclarationOrigin.LOGICAL_RULES_BODY,
                wasDescriptor,
                context.irBuiltIns.booleanType
            )
            +irRuleBody
            +irReturnFalse()
        }
    }

    private val addIteratorVisitor = object : IrElementVisitor<Unit, IrClass> {
        override fun visitElement(element: IrElement, data: IrClass) {
            element.acceptChildren(this, data)
        }

        override fun visitRuleIsOneOf(expression: IrRuleIsOneOf, data: IrClass) {
            if (expression.iterator == null) {
                val type = (expression.browse as IrCall).type
                val field = data.addField("iterator\$${expression.idx}", type, JavaVisibilities.PACKAGE_VISIBILITY)
                expression.iterator = field.symbol
            }
        }

        override fun visitRuleVariable(expression: IrRuleVariable, data: IrClass) {
            if (expression.field == null) {
                val name = "${expression.variable.name}$${expression.idx}"
                val field = data.addField(name, expression.variable.type, JavaVisibilities.PACKAGE_VISIBILITY)
                expression.field = field.symbol
            }
        }

        override fun visitRuleCall(expression: IrRuleCall, data: IrClass) {
            if (expression.iterator == null) {
                val field = data.addField("iterator\$${expression.idx}", expression.call.type, JavaVisibilities.PACKAGE_VISIBILITY)
                expression.iterator = field.symbol
            }
        }
    }

    private fun addIteratorFields(irRuleBody: IrRuleBody, frameClass: IrClass) {
        irRuleBody.accept(addIteratorVisitor, frameClass)
    }

    private fun rewriteLocalVariables(origFunction: IrFunction, bodyFunc: IrFunction, frameParam: IrValueParameter, frameClass: IrClass) {

        // parameters or original rule function are mapped onto fields of frame class
        val capturedValueToField: MutableMap<IrValueDeclaration, IrField> = mutableMapOf()
        origFunction.valueParameters.forEach { param ->
            val field = frameClass.declarations.find { it is IrField && it.name == param.name }
            if (field is IrField)
                capturedValueToField[param] = field
        }

        bodyFunc.transformChildrenVoid(object : IrElementTransformerVoid() {

            override fun visitGetValue(expression: IrGetValue): IrExpression {
                var expr: IrExpression = expression
                val field = capturedValueToField[expression.symbol.owner]
                if (field != null) {
                    val startOffset = expression.startOffset
                    val endOffset = expression.endOffset
                    expr = IrGetFieldImpl(
                        startOffset, endOffset, field.symbol,
                        type = field.type,
                        receiver = IrGetValueImpl(startOffset, endOffset, frameParam.type, frameParam.symbol)
                    )
                }
                expr.transformChildrenVoid();
                return expr
            }

            override fun visitSetVariable(expression: IrSetVariable): IrExpression {
                var expr: IrExpression = expression
                val field = capturedValueToField[expression.symbol.owner]
                if (field != null) {
                    val startOffset = expression.startOffset
                    val endOffset = expression.endOffset
                    expr = IrSetFieldImpl(
                        startOffset, endOffset, field.symbol,
                        receiver = IrGetValueImpl(startOffset, endOffset, frameParam.type, frameParam.symbol),
                        value = expression.value,
                        type = field.type
                    )
                }
                expr.transformChildrenVoid();
                return expr
            }

            override fun visitRuleVariable(declaration: IrRuleVariable): IrExpression {
                val field = frameClass.declarations.find { it is IrField && it.symbol == declaration.field }
                if (field is IrField)
                    capturedValueToField[declaration.variable] = field
                declaration.transformChildrenVoid();
                return declaration
            }


        })
    }
}

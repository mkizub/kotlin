/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common

import org.jetbrains.kotlin.backend.common.phaser.makeIrFilePhase
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOriginImpl
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrRuleBody
import org.jetbrains.kotlin.ir.expressions.impl.linkLogicalRules
import org.jetbrains.kotlin.ir.util.isRule
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid


val linkLogicalRulesPhase = makeIrFilePhase(
    ::LinkLogicalRulesLowering,
    "LinkLogicalRules",
    "Build state machine for logical rules and add continuation classes to rule methods"
)

private object LOGICAL_RULES_CLASS : IrDeclarationOriginImpl("LOGICAL_RULES_CLASS")

private class LinkLogicalRulesLowering(private val context: BackendContext) : FileLoweringPass {
//    private val ruleInterface by lazy {
//        context.getTopLevelClass(DescriptorUtils.LOGICAL_RULES_PACKAGE_FQ_NAME.child(Name.identifier("Rule")))
//    }
//    private val ruleBaseImpl by lazy {
//        context.getTopLevelClass(DescriptorUtils.LOGICAL_RULES_PACKAGE_FQ_NAME.child(Name.identifier("RuleImpl")))
//    }

    override fun lower(irFile: IrFile) {
        val ruleFunctions = collectRuleFunctions(irFile)
        for (rf in ruleFunctions) {
            linkLogicalRulesForFunction(rf)
            generateImplClassForFunction(rf)
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

    private fun generateImplClassForFunction(irFunction: IrFunction) {
    }
}

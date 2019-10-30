/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi2ir.generators

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.assertCast
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffsetSkippingComments
import org.jetbrains.kotlin.psi2ir.deparenthesize
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall

class RuleGenerator(
    bodyGenerator: BodyGenerator,
    scope: Scope
) : StatementGenerator(bodyGenerator, scope) {

    private fun KtElement.genRuleExpr(): IrRuleExpression {
        val unitType = this@RuleGenerator.context.irBuiltIns.unitType
        if (this is KtWhileExpressionBase) {
            val body = this.body
            if (body != null) {
                return IrRuleLeafImpl(
                    body.startOffsetSkippingComments, body.endOffset, unitType,
                    IrErrorExpressionImpl(
                        body.startOffsetSkippingComments,
                        body.endOffset,
                        unitType,
                        "(Do)While expressions may not have a body in rules"
                    ),
                    null
                )
            }
            val cond = this.condition
            if (cond != null) {
                val expr = cond.genExpr()
                return if (expr is IrExpression)
                    IrRuleWhileImpl(expr.startOffset, expr.endOffset, unitType, expr)
                else
                    IrRuleLeafImpl(
                        cond.startOffsetSkippingComments, cond.endOffset, unitType,
                        IrErrorExpressionImpl(
                            cond.startOffsetSkippingComments,
                            cond.endOffset,
                            unitType,
                            "A boolean expression is required"
                        ),
                        null
                    )
            }
        }
        if (this is KtBinaryExpression && this.operationToken == KtTokens.BACKTRACK) {
            val expr = this.left?.genExpr()
            val btrk = this.right?.genExpr()
            if (expr is IrRuleExpression || btrk is IrRuleExpression)
                return IrRuleLeafImpl(
                    this.startOffsetSkippingComments, this.endOffset, unitType,
                    IrErrorExpressionImpl(
                        startOffsetSkippingComments,
                        endOffset,
                        unitType,
                        "Left and right part of backtrack expressions may not be logical rules"
                    ),
                    null
                )
            return IrRuleLeafImpl(startOffsetSkippingComments, endOffset, unitType, expr?.assertCast(), btrk?.assertCast())
        }
        val stat = this.genExpr()
        if (stat is IrRuleExpression)
            return stat
        if (stat is IrVariable)
            return IrRuleVariableImpl(stat.startOffset, stat.endOffset, unitType, stat, null)
        if (stat is IrCall)
            return IrRuleCallImpl(stat.startOffset, stat.endOffset, unitType, stat, null)
        if (stat is IrExpression)
            return IrRuleLeafImpl(stat.startOffset, stat.endOffset, unitType, stat, null)
        throw AssertionError("Unexpected statement in rule body")
    }

    private fun KtElement.genExpr(): IrStatement {
        return try {
            deparenthesize().accept(this@RuleGenerator, null)
        } catch (e: Exception) {
            ErrorExpressionGenerator(this@RuleGenerator).generateErrorExpression(this, e)
        }
    }

    fun generateRuleExpression(ktStatement: KtExpression) =
        ktStatement.genRuleExpr()

    fun generateRuleExpressions(ktStatements: List<KtExpression>, to: IrRuleContainer) =
        ktStatements.mapTo(to.rules) { it.genRuleExpr() }

    override fun visitBlockExpression(expression: KtBlockExpression, data: Nothing?): IrStatement {
        val isBlockBody = expression.parent is KtDeclarationWithBody && expression.parent !is KtFunctionLiteral
        if (isBlockBody) throw AssertionError("Use IrBlockBody and corresponding body generator to generate blocks as function bodies")

        val returnType = context.irBuiltIns.unitType

        val irRuleExpr = if (expression.isCommaExpression)
            IrRuleAndImpl(expression.startOffsetSkippingComments, expression.endOffset, returnType)
        else
            IrRuleOrImpl(expression.startOffsetSkippingComments, expression.endOffset, returnType)

        expression.statements.forEach {
            irRuleExpr.rules.add(it.genRuleExpr())
        }

        return irRuleExpr
    }

    private fun isCutBaseExpr(expr: KtExpression?): Boolean =
        expr == null || KtPsiUtil.isBooleanConstant(expr)

    override fun visitPrefixExpression(expression: KtPrefixExpression, data: Nothing?): IrStatement {
        if (expression.operationToken == KtTokens.EXCLEXCL && isCutBaseExpr(expression.baseExpression))
            return IrRuleCutImpl(expression.startOffsetSkippingComments, expression.endOffset, context.irBuiltIns.unitType)
        if (expression.operationToken == KtTokens.EXCL) {
            val base = expression.baseExpression
            if (base == null)
                return IrRuleCutImpl(expression.startOffsetSkippingComments, expression.endOffset, context.irBuiltIns.unitType)
            else if (base is KtPrefixExpression && base.operationToken == KtTokens.EXCL && isCutBaseExpr(base.baseExpression))
                return IrRuleCutImpl(expression.startOffsetSkippingComments, expression.endOffset, context.irBuiltIns.unitType)
        }
        return super.visitPrefixExpression(expression, data)
    }
}

class RuleExpressionGenerator(statementGenerator: StatementGenerator) : StatementGeneratorExtension(statementGenerator) {

    private fun generateCall(
        resolvedCall: ResolvedCall<*>,
        ktExpression: KtExpression,
        origin: IrStatementOrigin?
    ) =
        CallGenerator(statementGenerator).generateCall(ktExpression, statementGenerator.pregenerateCall(resolvedCall), origin)

    fun generateRuleIsThe(expression: KtBinaryExpression): IrRuleIsThe {
        expression.left ?: throw AssertionError("Logical variable should be present:\n${expression.text}")
        val irGetVar = expression.left!!.genExpr() as IrDeclarationReference
        val unifyCall = getResolvedCall(expression)
            ?: throw AssertionError("No resolved call for unification operator ${expression.text}")
        val irUnifyCall = generateCall(unifyCall, expression, IrStatementOrigin.RULE_UNIFY)
        val resultType = context.irBuiltIns.unitType
        return IrRuleIsTheImpl(expression.startOffsetSkippingComments, expression.endOffset, resultType, irGetVar, irUnifyCall)
    }

    fun generateRuleIsOneOf(expression: KtBinaryExpression): IrRuleIsOneOf {
        expression.left ?: throw AssertionError("Logical variable should be present:\n${expression.text}")
        val irGetVar = expression.left!!.genExpr() as IrDeclarationReference
        expression.right ?: throw AssertionError("Container for values should be present:\n${expression.text}")
        val browseCall = getResolvedCall(expression)
            ?: throw AssertionError("No resolved call for browse operator ${expression.text}")
        val irBrowseCall = generateCall(browseCall, expression, IrStatementOrigin.RULE_BROWSE)
        val resultType = context.irBuiltIns.unitType
        return IrRuleIsOneOfImpl(expression.startOffsetSkippingComments, expression.endOffset, resultType, irGetVar, irBrowseCall, null)
    }
}
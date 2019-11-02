/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi2ir.generators

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.assertCast
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.builders.primitiveOp1
import org.jetbrains.kotlin.ir.builders.whenComma
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.types.isLogicalRule
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffsetSkippingComments
import org.jetbrains.kotlin.psi2ir.deparenthesize
import org.jetbrains.kotlin.psi2ir.intermediate.defaultLoad
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall

class RuleGenerator(
    bodyGenerator: BodyGenerator,
    scope: Scope
) : StatementGenerator(bodyGenerator, scope) {

    private fun wrapExpr(stat: IrStatement): IrRuleExpression {
        val unitType = this@RuleGenerator.context.irBuiltIns.unitType
        if (stat is IrRuleExpression)
            return stat
        if (stat is IrVariable)
            return IrRuleVariableImpl(stat.startOffset, stat.endOffset, unitType, stat, null)
        if (stat is IrCall && stat.type.isLogicalRule())
            return IrRuleCallImpl(stat.startOffset, stat.endOffset, unitType, stat, null)
        if (stat is IrExpression)
            return IrRuleLeafImpl(stat.startOffset, stat.endOffset, unitType, stat, null)
        throw AssertionError("Unexpected statement in rule body")
    }

    private fun KtElement.genRuleExpr(): IrRuleExpression {
        val unitType = this@RuleGenerator.context.irBuiltIns.unitType
        if (this is KtWhenExpression) {
            return generateWhen(this)
        }
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
        return wrapExpr(stat)
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
        val unitType = context.irBuiltIns.unitType

        val irRuleExpr = if (expression.isCommaExpression)
            IrRuleAndImpl(expression.startOffsetSkippingComments, expression.endOffset, unitType)
        else
            IrRuleOrImpl(expression.startOffsetSkippingComments, expression.endOffset, unitType)

        expression.statements.forEach {
            irRuleExpr.rules.add(it.genRuleExpr())
        }

        return irRuleExpr
    }

    override fun visitLambdaExpression(expression: KtLambdaExpression, data: Nothing?): IrStatement {
        val unitType = context.irBuiltIns.unitType

        val irRuleExpr = if (expression.bodyExpression?.isCommaExpression != false)
            IrRuleAndImpl(expression.startOffsetSkippingComments, expression.endOffset, unitType)
        else
            IrRuleOrImpl(expression.startOffsetSkippingComments, expression.endOffset, unitType)

        expression.bodyExpression?.let { body ->
            body.statements.forEach {
                irRuleExpr.rules.add(it.genRuleExpr())
            }
        }

        return irRuleExpr
    }

    private fun isCutBaseExpr(expr: KtExpression?): Boolean =
        expr == null || KtPsiUtil.isBooleanConstant(expr)

    private fun isCutFailExpr(expr: KtExpression?): Boolean =
        expr != null && KtPsiUtil.isFalseConstant(expr)

    override fun visitPrefixExpression(expression: KtPrefixExpression, data: Nothing?): IrStatement {
        if (expression.operationToken == KtTokens.EXCLEXCL && isCutBaseExpr(expression.baseExpression))
            return IrRuleCutImpl(
                expression.startOffsetSkippingComments, expression.endOffset,
                context.irBuiltIns.unitType, isCutFailExpr(expression.baseExpression)
            )
        if (expression.operationToken == KtTokens.EXCL) {
            val base = expression.baseExpression
            if (base == null)
                return IrRuleCutImpl(
                    expression.startOffsetSkippingComments, expression.endOffset,
                    context.irBuiltIns.unitType, true
                )
            else if (base is KtPrefixExpression && base.operationToken == KtTokens.EXCL && isCutBaseExpr(base.baseExpression))
                return IrRuleCutImpl(
                    expression.startOffsetSkippingComments, expression.endOffset,
                    context.irBuiltIns.unitType, isCutFailExpr(base.baseExpression)
                )
        }
        return super.visitPrefixExpression(expression, data)
    }

    override fun visitWhenExpression(expression: KtWhenExpression, data: Nothing?): IrStatement {
        return generateWhen(expression)
    }

    private fun generateWhen(expression: KtWhenExpression): IrRuleWhen {
        val unitType = context.irBuiltIns.unitType

        val irSubject = generateWhenSubject(expression)

        val irWhen = IrRuleWhenImpl(expression.startOffsetSkippingComments, expression.endOffset, unitType, IrStatementOrigin.WHEN)
        irWhen.subject = irSubject

        for (ktEntry in expression.entries) {
            if (ktEntry.isElse) {
                val irElseResult = wrapExpr(ktEntry.expression!!.genExpr())
                irWhen.branches.add(
                    IrElseBranchImpl(
                        IrConstImpl.boolean(irElseResult.startOffset, irElseResult.endOffset, context.irBuiltIns.booleanType, true),
                        irElseResult
                    )
                )
                break
            }

            var irBranchCondition: IrExpression? = null
            for (ktCondition in ktEntry.conditions) {
                val irCondition =
                    if (irSubject != null)
                        generateWhenConditionWithSubject(ktCondition, irSubject)
                    else
                        generateWhenConditionNoSubject(ktCondition)
                irBranchCondition = irBranchCondition?.let { context.whenComma(it, irCondition) } ?: irCondition
            }

            val irBranchResult = wrapExpr(ktEntry.expression!!.genExpr())
            irWhen.branches.add(IrBranchImpl(irBranchCondition!!, irBranchResult))
        }
        //addElseBranchForExhaustiveWhenIfNeeded(irWhen, expression)
        //return generateWhenBody(expression, irSubject, irWhen)
        return irWhen
    }

    private fun generateWhenSubject(expression: KtWhenExpression): IrVariable? {
        val subjectVariable = expression.subjectVariable
        val subjectExpression = expression.subjectExpression
        return when {
            subjectVariable != null -> visitProperty(subjectVariable, null) as IrVariable
            subjectExpression != null -> scope.createTemporaryVariable(subjectExpression.genExpr().assertCast(), "subject")
            else -> null
        }
    }

    private fun generateWhenConditionNoSubject(ktCondition: KtWhenCondition): IrExpression =
        (ktCondition as KtWhenConditionWithExpression).expression!!.genExpr().assertCast()

    private fun generateWhenConditionWithSubject(ktCondition: KtWhenCondition, irSubject: IrVariable): IrExpression {
        return when (ktCondition) {
            is KtWhenConditionWithExpression ->
                generateEqualsCondition(irSubject, ktCondition)
            is KtWhenConditionInRange ->
                generateInRangeCondition(irSubject, ktCondition)
            is KtWhenConditionIsPattern ->
                generateIsPatternCondition(irSubject, ktCondition)
            else ->
                throw AssertionError("Unexpected 'when' condition: ${ktCondition.text}")
        }
    }

    private fun generateIsPatternCondition(irSubject: IrVariable, ktCondition: KtWhenConditionIsPattern): IrExpression {
        val typeOperand = getOrFail(BindingContext.TYPE, ktCondition.typeReference)
        val irTypeOperand = typeOperand.toIrType()
        val irInstanceOf = IrTypeOperatorCallImpl(
            ktCondition.startOffsetSkippingComments, ktCondition.endOffset,
            context.irBuiltIns.booleanType,
            IrTypeOperator.INSTANCEOF,
            irTypeOperand,
            irSubject.defaultLoad()
        )
        return if (ktCondition.isNegated)
            primitiveOp1(
                ktCondition.startOffsetSkippingComments, ktCondition.endOffset,
                context.irBuiltIns.booleanNotSymbol,
                context.irBuiltIns.booleanType,
                IrStatementOrigin.EXCL,
                irInstanceOf
            )
        else
            irInstanceOf
    }

    private fun generateInRangeCondition(irSubject: IrVariable, ktCondition: KtWhenConditionInRange): IrExpression {
        val inCall = pregenerateCall(getResolvedCall(ktCondition.operationReference)!!)
        inCall.irValueArgumentsByIndex[0] = irSubject.defaultLoad()
        val inOperator = getInfixOperator(ktCondition.operationReference.getReferencedNameElementType())
        val irInCall = CallGenerator(this).generateCall(ktCondition, inCall, inOperator)
        return when (inOperator) {
            IrStatementOrigin.IN ->
                irInCall
            IrStatementOrigin.NOT_IN ->
                primitiveOp1(
                    ktCondition.startOffsetSkippingComments, ktCondition.endOffset,
                    context.irBuiltIns.booleanNotSymbol,
                    context.irBuiltIns.booleanType,
                    IrStatementOrigin.EXCL,
                    irInCall
                )
            else -> throw AssertionError("Expected 'in' or '!in', got $inOperator")
        }
    }

    private fun generateEqualsCondition(irSubject: IrVariable, ktCondition: KtWhenConditionWithExpression): IrExpression {
        val ktExpression = ktCondition.expression
        val irExpression = ktExpression!!.genExpr().assertCast<IrExpression>()
        return OperatorExpressionGenerator(this).generateEquality(
            ktCondition.startOffsetSkippingComments, ktCondition.endOffset, IrStatementOrigin.EQEQ,
            irSubject.defaultLoad(), irExpression,
            context.bindingContext[BindingContext.PRIMITIVE_NUMERIC_COMPARISON_INFO, ktExpression]
        )
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
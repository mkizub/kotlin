/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.expressions.impl

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrElementBase
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class IrRuleBodyImpl(
    startOffset: Int,
    endOffset: Int,
    override val originalFunctionSymbol: IrSimpleFunctionSymbol,
    override var expression: IrRuleExpression?
) :
    IrElementBase(startOffset, endOffset),
    IrRuleBody
{
    // function with generated state machine
    override var stateMachineFunctionSymbol: IrSimpleFunctionSymbol? = null
    // concrete frame class generated for this state machine
    override var frameClassSymbol: IrClassSymbol? = null

    // max backtrace stack depth
    override var depth: Int = 0
    // total states in state machine ('base' in rule expressions)
    override var states: Int = 0
    // total expressions in in state machine
    override var nodes: Int = 0

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R =
        visitor.visitRuleBody(this, data)

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        expression?.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        expression = expression?.transform(transformer, data) as IrRuleExpression
    }
}

fun IrRuleBody.linkLogicalRules(): IrRuleBody {
    this.accept(LinkVisitor(), IrRuleExpression.LinkData(null, null, false))
    return this
}

private class LinkVisitor : IrElementVisitor<Unit, IrRuleExpression.LinkData> {
    private var blockDepth = 0
    private var stateDepth = 0
    private var blockStates = 1
    private var totalNodes = 0

    fun allocNewState(n: Int = 1): Int {
        val b = blockStates
        blockStates += n
        return b
    }

    fun pushDepth(): Int {
        stateDepth += 1
        if (stateDepth > blockDepth)
            blockDepth = stateDepth
        return stateDepth - 1
    }

    fun setDepth(i: Int): Int {
        stateDepth = i
        if (stateDepth > blockDepth)
            blockDepth = stateDepth
        return stateDepth
    }


    override fun visitElement(element: IrElement, data: IrRuleExpression.LinkData) {
        // do nothing
    }

    override fun visitRuleBody(body: IrRuleBody, data: IrRuleExpression.LinkData) {
        blockDepth = 0
        stateDepth = 0
        blockStates = 1
        totalNodes = 0
        body.acceptChildren(this, IrRuleExpression.LinkData(null, null, false))
        body.depth = blockDepth
        body.states = blockStates
        body.nodes = totalNodes
    }

    override fun visitRuleOrExpression(expression: IrRuleOr, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleOrImpl
        expr.link = data
        expr.depth = stateDepth
        var maxDepth = stateDepth
        val max = expr.rules.size - 1
        for (i in 0..max) {
            stateDepth = expr.depth
            if (i == max) {
                expr.rules[i].accept(this, data)
            } else {
                expr.rules[i].accept(this, IrRuleExpression.LinkData(data.next, expr.rules[i + 1], true))
            }
            maxDepth = maxDepth.coerceAtLeast(stateDepth)
        }
        setDepth(maxDepth)
    }

    override fun visitRuleAndExpression(expression: IrRuleAnd, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleAndImpl
        expr.link = data
        expr.depth = stateDepth
        var jumpBack = data.jumpBack
        var back: IrRuleExpression? = data.back
        val max = expr.rules.size - 1
        for (i in 0 until max) {
            val r = expr.rules[i]
            r.accept(this, IrRuleExpression.LinkData(expr.rules[i + 1], back, jumpBack))
            if ((r is IrRuleLeaf && r.btrk != null) || r is IrRuleWhile) {
                back = r
                jumpBack = false
            } else if (r is IrRuleCut) {
                back = null
                jumpBack = false
            } else {
                back = r
                jumpBack = false
            }
        }
        if (max >= 0)
            expr.rules[max].accept(this, IrRuleExpression.LinkData(data.next, back, jumpBack))
    }

    override fun visitRuleCutExpression(expression: IrRuleCut, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleCutImpl
        expr.link = IrRuleExpression.LinkData(data.next, null, false)
        expr.idx = ++totalNodes
        expr.depth = stateDepth
    }

    override fun visitRuleLeafExpression(expression: IrRuleLeaf, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleLeafImpl
        expr.link = data
        expr.idx = ++totalNodes
        expr.depth = stateDepth
        if (expr.btrk != null) {
            expr.base = allocNewState()
            expr.depth = pushDepth()
        }
    }

    override fun visitRuleWhileExpression(expression: IrRuleWhile, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleWhileImpl
        expr.link = data
        expr.idx = ++totalNodes
        expr.base = allocNewState()
        expr.depth = pushDepth()
    }

    override fun visitRuleIsThe(expression: IrRuleIsThe, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleIsTheImpl
        expr.link = data
        expr.idx = ++totalNodes
        expr.base = allocNewState()
        expr.depth = pushDepth()
    }

    override fun visitRuleIsOneOf(expression: IrRuleIsOneOf, data: IrRuleExpression.LinkData) {
        val expr = expression as IrRuleIsOneOfImpl
        expr.link = data
        expr.idx = ++totalNodes
        expr.base = allocNewState()
        expr.depth = pushDepth()
    }
}

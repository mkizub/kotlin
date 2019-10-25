/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.expressions.impl

import org.jetbrains.kotlin.ir.assertCast
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.IrRuleExpression.LinkData
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.transform
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import java.util.*

abstract class IrRuleExpressionBase(
    startOffset: Int,
    endOffset: Int,
    type: IrType
) : IrExpressionBase(startOffset, endOffset, type),
    IrRuleExpression {

    override var depth: Int = 0
    override var base: Int = 0
    override var idx: Int = 0
    override var link: LinkData? = null
}

class IrRuleLeafImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleLeaf {
    constructor(startOffset: Int, endOffset: Int, type: IrType, expr: IrExpression?, btrk: IrExpression?) : this(startOffset, endOffset, type) {
        this.expr = expr
        this.btrk = btrk
    }

    override var expr: IrExpression? = null
    override var btrk: IrExpression? = null

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleLeafExpression(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        expr?.accept(visitor, data)
        btrk?.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        expr = expr?.transform(transformer, data)
        btrk = btrk?.transform(transformer, data)
    }
}

class IrRuleWhileImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleWhile {
    constructor(startOffset: Int, endOffset: Int, type: IrType, cond: IrExpression?) : this(startOffset, endOffset, type) {
        this.cond = cond
    }

    override var cond: IrExpression? = null

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleWhileExpression(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        cond?.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        cond = cond?.transform(transformer, data)
    }
}

class IrRuleCutImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleCut {
    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleCutExpression(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
    }
}

class IrRuleOrImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleOr {
    override var base
        get() = if (rules.isEmpty()) 0 else rules[0].base
        set(value) = throw AssertionError("Value for base state cannot be set")
    override var idx
        get() = if (rules.isEmpty()) 0 else rules[0].idx
        set(value) = throw AssertionError("Value for index cannot be set")

    override val rules: MutableList<IrRuleExpression> = ArrayList(2)

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleOrExpression(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        rules.forEach { it.accept(visitor, data) }
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        rules.transform { it.transform(transformer, data) }
    }
}

class IrRuleAndImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleAnd {
    override var base
        get() = if (rules.isEmpty()) 0 else rules[0].base
        set(value) = throw AssertionError("Value for base state cannot be set")
    override var idx
        get() = if (rules.isEmpty()) 0 else rules[0].idx
        set(value) = throw AssertionError("Value for index cannot be set")

    override val rules: MutableList<IrRuleExpression> = ArrayList(2)

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleAndExpression(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        rules.forEach { it.accept(visitor, data) }
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        rules.transform { it.transform(transformer, data) }
    }
}

class IrRuleIsTheImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType,
    override var target: IrGetValue,
    override var value: IrExpression
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleIsThe {

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleIsThe(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        target.accept(visitor, data)
        value.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        target = target.transform(transformer, data).assertCast()
        value = value.transform(transformer, data)
    }
}

class IrRuleIsOneOfImpl(
    startOffset: Int,
    endOffset: Int,
    type: IrType,
    override var target: IrGetValue,
    override var value: IrExpression
) : IrRuleExpressionBase(startOffset, endOffset, type),
    IrRuleIsOneOf {

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitRuleIsOneOf(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        target.accept(visitor, data)
        value.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        target = target.transform(transformer, data).assertCast()
        value = value.transform(transformer, data)
    }
}


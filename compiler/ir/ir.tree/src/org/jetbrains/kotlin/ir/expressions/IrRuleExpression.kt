/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.expressions

import org.jetbrains.kotlin.ir.symbols.IrFieldSymbol

interface IrRuleExpression : IrExpression {
    class LinkData(
        // next rule to check
        val next: IrRuleExpression?,
        // rule to backtrack
        val back: IrRuleExpression?,
        // use simple 'goto' for backtracking, instead of table switch
        val jumpBack: Boolean)

    // (max) depth of backtrack stack when flow control reaches this expression
    val depth: Int
    // base state (index) in state machine for this expression
    val base: Int
    // index of this expression in the function
    val idx: Int
    // link to next (forward) and previous (backtracking) expressions
    var link: LinkData?
}

interface IrRuleContainer {
    val rules: MutableList<IrRuleExpression>
}

interface IrRuleOr : IrRuleExpression, IrRuleContainer {
}

interface IrRuleAnd : IrRuleExpression, IrRuleContainer {
    override val base
        get() = if (rules.isEmpty()) 0 else rules[0].base
    override val idx
        get() = if (rules.isEmpty()) 0 else rules[0].idx
}

interface IrRuleIsThe : IrRuleExpression {
    val access: IrDeclarationReference
    val unify: IrExpression
}

interface IrRuleIsOneOf : IrRuleExpression {
    val access: IrDeclarationReference
    val browse: IrExpression
    var iterator: IrFieldSymbol?
}

interface IrRuleLeaf : IrRuleExpression {
    val expr: IrExpression?
    val btrk: IrExpression?
}

interface IrRuleWhile : IrRuleExpression {
    val cond: IrExpression?
}

interface IrRuleCut : IrRuleExpression {
}

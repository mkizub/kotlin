/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.jvm.codegen

import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.linkLogicalRules
import org.jetbrains.org.objectweb.asm.Label
import org.jetbrains.org.objectweb.asm.Type

class LogicalCodegenData(
    val env: IrValueParameter, // frame object parameter
    val btv: VariableInfo, // backtrack variable
    val switchLabel: Label,
    val stateLabels: Array<Label>,
    val enterLabels: Array<Label>
)

fun ExpressionCodegen.generateStateMachineForRule(body: IrRuleBody, data: BlockInfo) {
    body.linkLogicalRules()
    val env = irFunction.valueParameters.find { it.name.identifier == "frame$$" }!!
    val btv = data.variables.find { it.declaration.name.identifier == "bt$" }!!
    val switchLabel = Label()
    val stateLabels: Array<Label> = (0 until body.states).map { Label() }.toTypedArray()
    val enterLabels: Array<Label> = (0..body.nodes).map { Label() }.toTypedArray()
    logicalData = LogicalCodegenData(env, btv, switchLabel, stateLabels, enterLabels)

    // bt$ = env.bt$
    // if (bt$ < 0) {
    //   bt$ = 0;
    //   goto entry$1
    // }
    // switch(bt$) {
    // entry$0:
    // default:
    // case 0: return false;
    // entry$1: ...
    loadBacktrackState()
    mv.load(logicalData!!.btv.index, Type.INT_TYPE)
    mv.ifge(switchLabel)
    mv.iconst(0)
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
    if (enterLabels.size > 1)
        mv.goTo(enterLabels[1])
    else
        mv.goTo(enterLabels[0])
    mv.mark(switchLabel)
    mv.load(logicalData!!.btv.index, Type.INT_TYPE)
    mv.tableswitch(0, body.states - 1, stateLabels[0], *stateLabels)
    mv.mark(stateLabels[0])
    mv.mark(enterLabels[0])
    mv.iconst(0)
    mv.areturn(Type.BOOLEAN_TYPE)

    body.expression?.accept(this, data)

    mv.iconst(0)
    mv.areturn(Type.BOOLEAN_TYPE)

    logicalData = null
}

private fun ExpressionCodegen.loadBacktrackState(depth: Int) {
    // bt$ = env.bt$depth
    val env = loadFrameVar()
    val ownerType = typeMapper.mapType(env).internalName
    mv.getfield(ownerType, "bt\$$depth", "I")
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
}

private fun ExpressionCodegen.saveBacktrackState(depth: Int) {
    // env.bt$depth = bt$
    val env = loadFrameVar()
    mv.load(logicalData!!.btv.index, Type.INT_TYPE)
    val ownerType = typeMapper.mapType(env).internalName
    mv.putfield(ownerType, "bt\$$depth", "I")
}

private fun ExpressionCodegen.loadBacktrackState() {
    // bt$ = env.bt$
    val env = loadFrameVar()
    val ownerType = typeMapper.mapType(env).internalName
    mv.getfield(ownerType, "bt$", "I")
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
}

private fun ExpressionCodegen.saveBacktrackState() {
    // env.bt$ = bt$
    val env = loadFrameVar()
    mv.load(logicalData!!.btv.index, Type.INT_TYPE)
    val ownerType = typeMapper.mapType(env).internalName
    mv.putfield(ownerType, "bt$", "I")
}

private fun ExpressionCodegen.getFrameVar() =
    irFunction.valueParameters.find { it.name.identifier == "frame$$" }

private fun ExpressionCodegen.loadFrameVar(): IrValueParameter {
    val env = getFrameVar()!!
    mv.load(frameMap.getIndex(env.symbol), env.asmType)
    return env
}

private fun ExpressionCodegen.createCodeBacktrack(expr: IrRuleExpression, load: Boolean) {
    val link = expr.link!!
    val back = link.back
    when {
        back == null -> {
            // return false
            mv.iconst(0)
            mv.areturn(Type.BOOLEAN_TYPE)
        }
        link.jumpBack -> {
            if (load)
                loadBacktrackState(expr.depth)
            // goto "enter$link.back.idx"
            mv.goTo(logicalData!!.enterLabels[back.idx])
        }
        else -> {
            if (load)
                loadBacktrackState(expr.depth)
            // goto case bt$
            mv.goTo(logicalData!!.switchLabel)
        }
    }
}

private fun ExpressionCodegen.createCodeMoreCheck(expr: IrRuleExpression, force: Boolean) {
    val link = expr.link!!
    val next = link.next
    if (force || next == null)
        saveBacktrackState()
    if (next == null) {
        // return true
        mv.iconst(1)
        mv.areturn(Type.BOOLEAN_TYPE)
    } else if (force || next.idx != expr.idx + 1) {
        // goto "enter$link.back.idx"
        mv.goTo(logicalData!!.enterLabels[next.idx])
    }
}

fun ExpressionCodegen.generateRuleLeafExpression(expression: IrRuleLeaf, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    val expr = expression.expr
    val btrk = expression.btrk
    if (expr != null) {
        if (expr.type == context.irBuiltIns.booleanType) {
            // if (!expr)
            //   backtrack
            // more check
            val elseLabel = Label()
            expr.accept(this, data).coerceToBoolean().jumpIfTrue(elseLabel)
            createCodeBacktrack(expression, false)
            mv.mark(elseLabel)
            if (btrk != null) {
                saveBacktrackState(expression.depth)
                // bt$ = N
                mv.iconst(expression.base)
                mv.store(logicalData!!.btv.index, Type.INT_TYPE)
            }
            createCodeMoreCheck(expression, btrk != null)
        } else if (btrk == null) {
            // expr
            // more check
            expr.accept(this, data).discard()
            createCodeMoreCheck(expression, false)
        } else {
            saveBacktrackState(expression.depth)
            // bt$ = N
            // expr
            // more check
            mv.iconst(expression.base)
            mv.store(logicalData!!.btv.index, Type.INT_TYPE)
            expr.accept(this, data).discard()
            createCodeMoreCheck(expression, true)
        }
    } else {
        createCodeMoreCheck(expression, btrk != null)
    }

    if (btrk != null) {
        // case N:
        mv.mark(logicalData!!.stateLabels[expression.base])
        // btrk
        // backtrack
        btrk.accept(this, data).discard()
        createCodeBacktrack(expression, true)
    }
    return immaterialUnitValue
}

fun ExpressionCodegen.generateRuleWhileExpression(expression: IrRuleWhile, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    // env.bt$depth = bt$
    saveBacktrackState(expression.depth)
    // bt$ = N
    mv.iconst(expression.base)
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
    // case N:
    mv.mark(logicalData!!.stateLabels[expression.base])
    // if (!cond)
    //   backtrack
    // more check
    val cond = expression.cond
    val elseLabel = Label()
    if (cond != null) {
        cond.accept(this, data).coerceToBoolean().jumpIfTrue(elseLabel)
    } else {
        mv.goTo(elseLabel) // same as while(true) ...
    }
    createCodeBacktrack(expression, false)
    mv.mark(elseLabel)
    createCodeMoreCheck(expression, false)

    return immaterialUnitValue
}

fun ExpressionCodegen.generateRuleCutExpression(expression: IrRuleCut, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    // bt$ = 0
    // more check
    mv.iconst(0)
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
    createCodeMoreCheck(expression, false)

    return immaterialUnitValue
}


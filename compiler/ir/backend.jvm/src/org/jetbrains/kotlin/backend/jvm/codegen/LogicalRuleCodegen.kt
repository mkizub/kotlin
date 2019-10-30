/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.jvm.codegen

import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.linkLogicalRules
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isIterator
import org.jetbrains.org.objectweb.asm.Label
import org.jetbrains.org.objectweb.asm.Type

class LogicalCodegenData(
    val env: IrValueParameter, // frame object parameter
    val btv: VariableInfo, // backtrack variable (bt$)
    val wbv: VariableInfo, // was bound variable (was$bound)
    val switchLabel: Label,
    val stateLabels: Array<Label>,
    val enterLabels: Array<Label>
)

fun ExpressionCodegen.generateStateMachineForRule(body: IrRuleBody, data: BlockInfo) {
    body.linkLogicalRules()
    val env = irFunction.valueParameters.find { it.name.identifier == "frame$$" }!!
    val btv = data.variables.find { it.declaration.name.identifier == "bt$" }!!
    val wbv = data.variables.find { it.declaration.name.identifier == "was\$bound" }!!
    val switchLabel = Label()
    val stateLabels: Array<Label> = (0 until body.states).map { Label() }.toTypedArray()
    val enterLabels: Array<Label> = (0..body.nodes).map { Label() }.toTypedArray()
    logicalData = LogicalCodegenData(env, btv, wbv, switchLabel, stateLabels, enterLabels)

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

private fun ExpressionCodegen.loadFrameVar(): IrValueParameter {
    val env = logicalData!!.env
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
        @Suppress("CascadeIf")
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

@Suppress("UNUSED_PARAMETER")
fun ExpressionCodegen.generateRuleCutExpression(expression: IrRuleCut, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    // bt$ = 0
    // more check
    mv.iconst(0)
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
    createCodeMoreCheck(expression, false)

    return immaterialUnitValue
}

fun ExpressionCodegen.generateRuleIsThe(expression: IrRuleIsThe, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    // was$bound = target.bound()
    // if (!target.unify(value))
    //   backtrack
    // if (was$bound) {
    //   env.bt$depth = bt$
    //   bt$ = N
    // }
    //   more check

    val access = expression.access
    val unify = expression.unify

    val unboundLabel = Label()
    val unifiedLabel = Label()

    access.accept(this, data).materialize()
    mv.invokevirtual("kotlin/PVar", "bound", "()Z", false)
    mv.store(logicalData!!.wbv.index, Type.BOOLEAN_TYPE)

    unify.accept(this, data).materialize()
    mv.ifne(unifiedLabel)
    createCodeBacktrack(expression, false)

    mv.mark(unifiedLabel)
    mv.load(logicalData!!.wbv.index, Type.BOOLEAN_TYPE)
    mv.ifne(unboundLabel)

    saveBacktrackState(expression.depth)
    mv.iconst(expression.base)
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)

    mv.mark(unboundLabel)
    createCodeMoreCheck(expression, true)

    // case N:
    //  target.unbind()
    //  backtrack
    mv.mark(logicalData!!.stateLabels[expression.base])

    access.accept(this, data).materialize()
    mv.invokevirtual("kotlin/PVar", "unbind", "()V", false)
    createCodeBacktrack(expression, true)

    return immaterialUnitValue
}

fun ExpressionCodegen.generateRuleIsOneOf(expression: IrRuleIsOneOf, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    // if (target.bound()) {
    //   if (target.browse(arg) != null)
    //     more check
    //   backtrack
    // }
    //
    //   env.iterator$idx = target.browse(arg)
    //   if (env.iterator$idx == null)
    //     backtrack
    //   env.bt$depth = bt$
    //   bt$ = N
    //   more check
    // case N:
    //   target.unbind()
    //   if (target.browse(env.iterator$idx) != null)
    //     more check
    //   env.iterator$idx = null
    //   target.unbind()
    //   backtrack


    val access = expression.access
    val browse = expression.browse as IrCall

    // if (target.bound()) {
    //   if (target.browse(arg) != null)
    //     more check
    //   backtrack
    // }
    //
    run {
        val unboundLabel = Label()
        access.accept(this, data).materialize()
        mv.invokevirtual("kotlin/PVar", "bound", "()Z", false)
        mv.ifeq(unboundLabel)

        val nullLabel = Label()
        browse.accept(this, data).materialize()
        mv.ifnull(nullLabel)
        createCodeMoreCheck(expression, true)
        mv.mark(nullLabel)
        createCodeBacktrack(expression, false)
        mv.mark(unboundLabel)
    }

    // env.iterator$idx = target.browse(arg)
    // if (env.iterator$idx == null)
    //   backtrack
    val env = logicalData!!.env
    run {
        loadFrameVar()
        browse.accept(this, data).materialize()
        val ownerType = typeMapper.mapType(env).internalName
        val valueType = typeMapper.mapType(expression.iterator!!.owner.type).descriptor
        mv.putfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
        val notnullLabel = Label()
        loadFrameVar()
        mv.getfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
        mv.ifnonnull(notnullLabel)
        createCodeBacktrack(expression, false)
        mv.mark(notnullLabel)
    }
    //   env.bt$depth = bt$
    //   bt$ = N
    //   more check
    saveBacktrackState(expression.depth)
    mv.iconst(expression.base)
    mv.store(logicalData!!.btv.index, Type.INT_TYPE)
    createCodeMoreCheck(expression, true)

    // case N:
    mv.mark(logicalData!!.stateLabels[expression.base])
    //   target.unbind()
    access.accept(this, data).materialize()
    mv.invokevirtual("kotlin/PVar", "unbind", "()V", false)
    //   if (target.browse(env.iterator$idx) != null)
    //     more check
    run {
        // lookup for browse(iterator)
        val browse_iter = (browse.symbol.owner.parent as IrClass).declarations.find { decl ->
            decl is IrFunction && decl.name.toString() == "browse"
                    && decl.valueParameters.size == 1 && decl.valueParameters[0].type.isIterator()
        } as IrFunction
        val browse_type = IrSimpleTypeImpl(
            null,
            (browse_iter.returnType as IrSimpleType).classifier,
            true,
            listOf(),
            listOf(),
            null
        )
        val browse_call =
            IrCallImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, browse_type, browse_iter.symbol, browse_iter.symbol.descriptor, 0, 1)
        browse_call.putValueArgument(
            0, IrGetFieldImpl(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                expression.iterator!!,
                browse.type,
                IrGetValueImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, env.symbol)
            )
        )
        browse_call.dispatchReceiver = access
        val elseLabel = Label()
        browse_call.accept(this, data).materialize()
        mv.ifnull(elseLabel)
        createCodeMoreCheck(expression, true)
        mv.mark(elseLabel)

    }
    //   env.iterator$idx = null
    //   target.unbind()
    //   backtrack
    run {
        loadFrameVar()
        mv.aconst(null)
        val ownerType = typeMapper.mapType(env).internalName
        val valueType = typeMapper.mapType(expression.iterator!!.owner.type).descriptor
        mv.putfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
    }
    access.accept(this, data).materialize()
    mv.invokevirtual("kotlin/PVar", "unbind", "()V", false)
    createCodeBacktrack(expression, true)

    return immaterialUnitValue
}

fun ExpressionCodegen.generateRuleVariable(expression: IrRuleVariable, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    // initialize frame's field on entering
    // maybe we shell add a state to cleanup value of backtracking?

    //   env.variable$idx = initializer
    //   more check
    expression.variable.initializer?.let { initializer ->
        val env = loadFrameVar()
        initializer.accept(this, data).materialize()
        val ownerType = typeMapper.mapType(env).internalName
        val valueType = typeMapper.mapType(expression.field!!.owner.type).descriptor
        mv.putfield(ownerType, expression.field!!.owner.name.asString(), valueType)
    }

    return immaterialUnitValue
}

fun ExpressionCodegen.generateRuleCall(expression: IrRuleCall, data: BlockInfo): PromisedValue {
    mv.mark(logicalData!!.enterLabels[expression.idx])

    //   env.iterator$idx = call
    //   env.bt$depth = bt$
    //   bt$ = N
    // case N:
    //   if (env.iterator$idx.hasNext())
    //     env.iterator$idx.next()
    //     more check
    //   env.iterator$idx = null
    //   backtrack

    // env.iterator$idx = target.browse(arg)
    //   env.bt$depth = bt$
    //   bt$ = N
    val env = logicalData!!.env
    run {
        loadFrameVar()
        expression.call.accept(this, data).materialize()
        val ownerType = typeMapper.mapType(env).internalName
        val valueType = typeMapper.mapType(expression.iterator!!.owner.type).descriptor
        mv.putfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
        saveBacktrackState(expression.depth)
        mv.iconst(expression.base)
        mv.store(logicalData!!.btv.index, Type.INT_TYPE)
    }
    // case N:
    mv.mark(logicalData!!.stateLabels[expression.base])
    //   if (env.iterator$idx.hasNext())
    //     env.iterator$idx.next()
    //     more check
    run {
        val backtrackLabel = Label()
        val ownerType = typeMapper.mapType(env).internalName
        val valueType = typeMapper.mapType(expression.iterator!!.owner.type).descriptor
        loadFrameVar()
        mv.getfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
        mv.invokeinterface("java/util/Iterator", "hasNext", "()Z")
        mv.ifeq(backtrackLabel)
        loadFrameVar()
        mv.getfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
        mv.invokeinterface("java/util/Iterator", "next", "()Ljava/lang/Object;")
        mv.pop()
        createCodeMoreCheck(expression, true)
        mv.mark(backtrackLabel)
    }

    //   env.iterator$idx = null
    //   backtrack
    run {
        loadFrameVar()
        mv.aconst(null)
        val ownerType = typeMapper.mapType(env).internalName
        val valueType = typeMapper.mapType(expression.iterator!!.owner.type).descriptor
        mv.putfield(ownerType, expression.iterator!!.owner.name.asString(), valueType)
        createCodeBacktrack(expression, true)
    }

    return immaterialUnitValue
}


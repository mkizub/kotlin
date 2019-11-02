/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.ir.expressions

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer

interface IrBody : IrElement {
    override fun <D> transform(transformer: IrElementTransformer<D>, data: D): IrBody =
        accept(transformer, data) as IrBody
}

interface IrExpressionBody : IrBody {
    var expression: IrExpression

    override fun <D> transform(transformer: IrElementTransformer<D>, data: D): IrExpressionBody =
        accept(transformer, data) as IrExpressionBody
}

interface IrBlockBody : IrBody, IrStatementContainer

interface IrRuleBody : IrBody, IrStatement {

    var expression: IrRuleExpression?

    // max backtrace stack depth
    var depth: Int
    // total states in state machine ('base' in rule expressions)
    var states: Int
    // total expressions in in state machine
    var nodes: Int

    override fun <D> transform(transformer: IrElementTransformer<D>, data: D): IrRuleBody =
        accept(transformer, data) as IrRuleBody
}

interface IrSyntheticBody : IrBody {
    val kind: IrSyntheticBodyKind
}

enum class IrSyntheticBodyKind {
    ENUM_VALUES,
    ENUM_VALUEOF
}


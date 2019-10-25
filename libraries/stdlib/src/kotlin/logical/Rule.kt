/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("RuleKt")
@file:Suppress("unused")

package kotlin.logical

interface Rule {
    /**
     * Returns an [Iterator] that returns Unit.
     * Iterator's hasNext() returns true is next solution found
     */
    operator fun iterator(): Iterator<Unit>
}

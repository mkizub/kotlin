/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

//@file:kotlin.jvm.JvmMultifileClass
//@file:kotlin.jvm.JvmName("RuleKt")
//@file:Suppress("unused")

// Temporary moved to core project, before it appears and standard library

package kotlin.logical

interface Rule {
    /**
     * Returns an [Iterator] that returns Unit.
     * Iterator's hasNext() returns true is next solution found
     */
    operator fun iterator(): Iterator<Boolean>
}

private typealias State = Int

private const val State_NotReady: State = 0
private const val State_Ready: State = 1
private const val State_Done: State = 2
private const val State_Failed: State = 3

abstract class RuleFrame : Rule, Iterator<Boolean> {

    final override fun iterator(): Iterator<Boolean> = this

    private var state: State = State_NotReady

    protected abstract fun calculate(): Boolean

    final override fun hasNext(): Boolean {
        return when (state) {
            State_NotReady -> {
                if (calculate()) {
                    state = State_Ready; true
                } else {
                    state = State_Done; false
                }
            }
            State_Done -> false
            State_Ready -> true
            else -> throw exceptionalState()
        }
    }

    final override fun next(): Boolean {
        return when (state) {
            State_NotReady -> nextNotReady()
            State_Ready -> {
                state = State_NotReady; true
            }
            else -> throw exceptionalState()
        }
    }

    private fun nextNotReady(): Boolean {
        if (!hasNext()) throw NoSuchElementException() else return next()
    }

    private fun exceptionalState(): Throwable = when (state) {
        State_Done -> NoSuchElementException()
        State_Failed -> IllegalStateException("Iterator has failed.")
        else -> IllegalStateException("Unexpected state of the iterator: $state")
    }
}
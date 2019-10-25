/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin

/**
 * The wrapper for logical (prolog) values
 */

@Suppress("unused")
class PVar<A>(var value: A? = null) {
    val bound: Boolean
        get() = (value != null)

    fun bind(v: A) {
        value = v
    }

    fun unbind() {
        value = null
    }

    fun unify(v: A): Boolean {
        return if (value == null) {
            value = v
            true
        } else {
            value == v
        }
    }

    fun reunify(v: A): Boolean {
        value = null
        value = v
        return true
    }

    fun browse(iterator: Iterator<A>): Boolean {
        for (v in iterator) {
            if (value == null || value == v)
                return true
        }
        return false
    }

    fun browse(iterable: Iterable<A>): Boolean {
        return browse(iterable.iterator())
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return value == other
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}


// TARGET_BACKEND: JVM
// FILE: inline.kt
// KOTLIN_CONFIGURATION_FLAGS: ASSERTIONS_MODE=jvm
// WITH_RUNTIME
// FULL_JDK
package test

inline fun inlineMe(crossinline c : () -> String) = {
    assert(true)
    c()
}

// FILE: inlineSite.kt
// KOTLIN_CONFIGURATION_FLAGS: ASSERTIONS_MODE=jvm
import test.*

fun box(): String = (inlineMe { "OK" })()

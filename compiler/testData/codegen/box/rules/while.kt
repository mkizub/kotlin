// TARGET_BACKEND: JVM_IR
// WITH_RUNTIME
rule fun foo() {
    do while (decrement() > 0),
    pass()
}
private var counter = 3
fun decrement(): Int {
    trace("dec")
    return --counter
}
fun pass(): Boolean {
    trace("pass")
    return true
}
val expected = listOf<String>("dec","pass","dec","pass","dec")
val result = mutableListOf<String>()
fun trace(msg: String) {    
    result.add(msg)
}
fun box(): String {
    for (r in foo()) {
        if (!r)
            return "Fail"
    }
    if (result != expected)
        return "Fail"
    return "OK"
}

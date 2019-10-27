// TARGET_BACKEND: JVM_IR
// WITH_RUNTIME
rule fun foo() {
    test(),
    cond() ?< btrk(),
    done()
}
fun test(): Boolean {
    trace("test")
    return true
}
fun cond(): Boolean {
    trace("cond")
    return true
}
fun btrk() {
    trace("btrk")
}
fun done(): Boolean {
    trace("done")
    return true
}
val expected = listOf<String>("test","cond","done","btrk")
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

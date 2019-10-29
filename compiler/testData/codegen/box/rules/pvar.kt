// TARGET_BACKEND: JVM_IR
// WITH_RUNTIME
rule fun foo(p: PVar<String>, scalar: String, list: List<String>, iter: Iterator<String>) {
    p ?= scalar
;
    p @= list
;
    p @= iter
;
    p ?= "violet"
}
val expected = listOf<String>("red","yellow","orange","green","blue","violet")
val result = mutableListOf<String>()
fun box(): String {
    val p = PVar<String>()
    for (r in foo(p,"red",listOf("yellow","orange"),listOf("green","blue").iterator())) {
        if (!r)
            return "Fail"
        result.add(p.value!!)
    }
    if (expected != result)
        return "Fail"
    return "OK"
}

// TARGET_BACKEND: JVM_IR
// WITH_RUNTIME
rule fun foo(p: PVar<Int>) {
    val x = PVar<Int>()
    val y = 3

    p ?= 1
;
    x ?= 2,
    p ?= x
;
    p ?= y
;
    bar(x),
    p ?= x
}

rule fun bar(p: PVar<Int>) {
    p ?= 4
;
    p @= 5..7
}

val expected = listOf(1, 2, 3, 4, 5, 6, 7)
val result = mutableListOf<Int>()
fun box(): String {
    val p = PVar<Int>()
    for (r in foo(p)) {
        if (!r)
            return "Fail"
        p.value?.let {
            result.add(it)
        }
    }
    if (expected != result)
        return "Fail"
    return "OK"
}

fun main() {
    println(box())
}

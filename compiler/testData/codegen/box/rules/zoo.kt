rule fun foo() {
    val x = PVar<Int>()
    x ?= 3
;   x @= 1..6
;   {
    ;   bar(x)
    ;   baz()
    }
}
rule fun bar(x: PVar<Int>) {
    {
        x ?= 100
    ;   1000
    },
    {
        x ?= 200
        x ?= 300
    }
}

fun baz() = false

fun box(): String {
    for (r in foo()) {
        if (!r)
            return "Fail"
    }
    return "OK"
}

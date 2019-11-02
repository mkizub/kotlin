// TARGET_BACKEND: JVM_IR
// WITH_RUNTIME
rule fun foo(i: PVar<Int>) {
    trace("enter") ?< trace("leave"),
    when {
        !i.bound() -> {
            trace("unbound"),
            i @= 1..3
        }
        else -> {
            trace("bound to $i"),
            i.value!! > 0
        }
    },
    trace(i.toString())
}
rule fun bar(i: PVar<Any>) {
    trace("enter") ?< trace("leave"),
    when (i.value) {
        null -> trace("unbound")
        is Int -> trace("int")
        is String -> trace("string")
    },
    when (i.value) {
        1 -> trace("I")
        2 -> trace("II")
        3 -> trace("III")
        4 -> trace("IV")
        "s" -> trace("s1")
        "st" -> trace("s2")
        "str" -> trace("s3")
        else -> trace("?")
    },
    trace(i.toString())
}
val expected1 = listOf<String>("enter","unbound","1","2","3","leave")
val expected2 = listOf<String>("enter","bound to 5","5","leave")
val expected3 = listOf<String>("enter","bound to 0","leave")
val expected4 = listOf<String>("enter","unbound","?","null","leave")
val expected5 = listOf<String>("enter","int","IV","4","leave")
val expected6 = listOf<String>("enter","string","s3","str","leave")
val expected7 = listOf<String>("enter","leave")
val result = mutableListOf<String>()
fun trace(msg: String) {
    result.add(msg)
}
fun box(): String {
    run {
        result.clear()
        val i = PVar<Int>()
        for (r in foo(i)) {
            if (!r)
                return "Fail"
            println("1: step: ${i}")
        }
        println("1: result: ${result}")
        if (result != expected1)
            return "Fail"
    }
    run {
        result.clear()
        val i = PVar<Int>(5)
        for (r in foo(i)) {
            if (!r)
                return "Fail"
            println("2: step: ${i}")
        }
        println("2: result: ${result}")
        if (result != expected2)
            return "Fail"
    }
    run {
        result.clear()
        val i = PVar<Int>(0)
        for (r in foo(i)) {
            if (!r)
                return "Fail"
            println("3: step: ${i}")
        }
        println("3: result: ${result}")
        if (result != expected3)
            return "Fail"
    }
    run {
        result.clear()
        val i = PVar<Any>()
        for (r in bar(i)) {
            if (!r)
                return "Fail"
            println("4: step: ${i}")
        }
        println("4: result: ${result}")
        if (result != expected4)
            return "Fail"
    }
    run {
        result.clear()
        val i = PVar<Any>(4)
        for (r in bar(i)) {
            if (!r)
                return "Fail"
            println("5: step: ${i}")
        }
        println("5: result: ${result}")
        if (result != expected5)
            return "Fail"
    }
    run {
        result.clear()
        val i = PVar<Any>("str")
        for (r in bar(i)) {
            if (!r)
                return "Fail"
            println("6: step: ${i}")
        }
        println("6: result: ${result}")
        if (result != expected6)
            return "Fail"
    }
    run {
        result.clear()
        val i = PVar<Any>(true)
        for (r in bar(i)) {
            if (!r)
                return "Fail"
            println("7: step: ${i}")
        }
        println("7: result: ${result}")
        if (result != expected7)
            return "Fail"
    }
    return "OK"
}

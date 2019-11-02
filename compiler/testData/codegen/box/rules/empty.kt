// TARGET_BACKEND: JVM_IR
rule fun foo() {
}
fun box(): String {
    val i = foo() as kotlin.logical.RuleFrame
    if (i.hasNext())
        return "Fail"
    var throwed = false
    try {
        if (i.next())
            return "Fail"
    } catch (e: NoSuchElementException) {
        throwed = true
    } catch (t: Throwable) {
        return "Fail"
    }
    if (throwed)
        return "Fail"
    return "OK"
}

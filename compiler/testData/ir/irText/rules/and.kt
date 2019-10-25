rule fun foo(pv: PVar<Int>) {
    1,
    "",
    true,
    pv ?= 3,
    pv @= 1..10,
    !!,
    do while ((pv.value?.dec() ?: 0) > 0),
    200 ?< 300
}

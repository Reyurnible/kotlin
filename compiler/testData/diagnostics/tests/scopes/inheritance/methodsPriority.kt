// !DIAGNOSTICS: -UNUSED_VARIABLE
// FILE: A.java
class A {
    int foo() {return 1}
}

// FILE: 1.kt

fun foo() = ""

open class B: A() {
    init {
        val a: Int = foo()
    }
}

fun test() {
    fun foo() = ""

    class B: A() {
        init {
            val a: Int = <!TYPE_MISMATCH!>foo()<!> // todo
        }
    }
}

class E: A() {
    <!VIRTUAL_MEMBER_HIDDEN!>fun foo()<!> = A()

    init {
        val a: A = foo() // todo: discuss
    }
}
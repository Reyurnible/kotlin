// !DIAGNOSTICS: -UNUSED_PARAMETER

interface Foo
interface Bar

<!CONFLICTING_OVERLOADS!>fun <T> foo(x: T): T<!> where T: Foo, T: Bar {null!!}
<!CONFLICTING_OVERLOADS!>fun foo(x: Foo): Foo<!> {null!!}
fun foo(x: Bar): Bar {null!!}

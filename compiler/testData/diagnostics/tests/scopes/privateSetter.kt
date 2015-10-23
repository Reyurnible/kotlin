public class MyThing<A> {
    var name: String = "Joe"
        private set

}

fun main(args: Array<String>) {
    val myJoe = MyThing<Any>()
    <!INVISIBLE_SETTER!>myJoe.name<!> = "Jane"
}
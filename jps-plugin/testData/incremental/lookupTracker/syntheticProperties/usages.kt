package foo.bar

import /*p:<root>*/JavaClass
import foo./*p:foo*/KotlinClass

/*p:foo.bar*/fun main() {
    val j = /*p:foo.bar*/JavaClass()
    val k = /*p:foo.bar*/KotlinClass()

    j.getFoo()
    j./*p:foo.bar c:JavaClass(getFoo) c:JavaClass(getFOO) c:JavaClass(setFoo)*/foo
    j./*p:foo.bar c:JavaClass(getBar) c:JavaClass(getBAR) c:JavaClass(setBar)*/bar
    j./*p:foo.bar c:JavaClass(getBar) c:JavaClass(getBAR) c:JavaClass(setBar)*/bar = ""
//    j./**p:foo.bar c:JavaClass(getBazBaz) c:JavaClass(getBAZBaz)*/bazBaz
//    j.bazBaz = ""
    k./*c:foo.KotlinClass*/getFoo()
    k./*c:foo.KotlinClass p:foo.bar c:foo.KotlinClass(getFoo) c:foo.KotlinClass(getFOO) c:foo.KotlinClass(setFoo)*/foo
    k./*c:foo.KotlinClass p:foo.bar c:foo.KotlinClass(getBar) c:foo.KotlinClass(getBAR) c:foo.KotlinClass(setBar)*/bar
    k./*c:foo.KotlinClass p:foo.bar c:foo.KotlinClass(getBar) c:foo.KotlinClass(getBAR) c:foo.KotlinClass(setBar)*/bar = ""
//    k./**c:foo.KotlinClass p:foo.bar c:foo.KotlinClass(getBazBaz) c:foo.KotlinClass(getBAZBaz)*/bazBaz
//    k.bazBaz = ""
}

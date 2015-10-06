package test
@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
internal annotation class InternalTestAnnotation()

internal open class InternalClass1

abstract class ClassA1(internal val member: Int)

abstract class ClassB1 {
    internal abstract val member: Int
}


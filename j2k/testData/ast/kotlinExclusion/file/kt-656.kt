package demo
import java.util.Iterator
open class Test() : java.lang.Iterable<String?> {
override public fun iterator() : java.util.Iterator<String?>? {
return null
}
open public fun push(i : java.util.Iterator<String?>?) : java.util.Iterator<String?>? {
var j : java.util.Iterator<String?>? = i
return j
}
}
open class FullTest() : java.lang.Iterable<String?> {
override public fun iterator() : java.util.Iterator<String?>? {
return null
}
open public fun push(i : java.util.Iterator<String?>?) : java.util.Iterator<String?>? {
var j : java.util.Iterator<String?>? = i
return j
}
}
namespace test
open class Test() : Base() {
override public fun hashCode() : Int {
return super.hashCode()
}
override public fun equals(o : Any?) : Boolean {
return super.equals(o)
}
override protected fun clone() : Any? {
return super.clone()
}
override public fun toString() : String? {
return super.toString()
}
override protected fun finalize() : Unit {
super.finalize()
}
}
open class Base() {
open public fun hashCode() : Int {
return super.hashCode()
}
open public fun equals(o : Any?) : Boolean {
return super.equals(o)
}
open protected fun clone() : Any? {
return super.clone()
}
open public fun toString() : String? {
return super.toString()
}
open protected fun finalize() : Unit {
super.finalize()
}
}
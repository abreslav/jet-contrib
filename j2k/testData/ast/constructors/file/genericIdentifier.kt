public open class Identifier<T>(_myName : T?, _myHasDollar : Boolean) {
private val myName : T? = null
private var myHasDollar : Boolean = false
private var myNullable : Boolean = true
open public fun getName() : T? {
return myName
}
{
myName = _myName
myHasDollar = _myHasDollar
}
class object {
open public fun init<T>(name : T?) : Identifier<T> {
val __ = Identifier(name, false)
return __
}
open public fun init<T>(name : T?, isNullable : Boolean) : Identifier<T> {
val __ = Identifier(name, false)
__.myNullable = isNullable
return __
}
open public fun init<T>(name : T?, hasDollar : Boolean, isNullable : Boolean) : Identifier<T> {
val __ = Identifier(name, hasDollar)
__.myNullable = isNullable
return __
}
}
}
public open class User() {
class object {
open public fun main() : Unit {
var i1 : Identifier<*>? = Identifier.init<String?>("name", false, true)
var i2 : Identifier<Any?>? = Identifier.init<String?>("name", false)
var i3 : Identifier<Any?>? = Identifier.init<String?>("name")
}
}
}
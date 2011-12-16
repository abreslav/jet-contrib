namespace com.voltvoodoo.saplo4j.model
import java.io.Serializable
public open class Language(code : String?) : Serializable {
{
this.code = code
}
protected var code : String? = null
override public fun toString() : String? {
return this.code
}
}
open class Base() {
open fun test() : Unit {
}
open fun toString() : String? {
return "BASE"
}
}
open class Child() : Base() {
override fun test() : Unit {
}
override fun toString() : String? {
return "Child"
}
}
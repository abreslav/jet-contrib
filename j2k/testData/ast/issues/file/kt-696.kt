namespace test
open class Base() {
open public fun hashCode() : Int {
return System.identityHashCode(this)
}
open public fun equals(o : Any?) : Boolean {
return this.identityEquals(o)
}
open public fun toString() : String? {
return getJavaClass<Base>.getName() + '@' + Integer.toHexString(hashCode())
}
}
open class Child() : Base() {
override public fun hashCode() : Int {
return super.hashCode()
}
override public fun equals(o : Any?) : Boolean {
return super.equals(o)
}
override public fun toString() : String? {
return super.toString()
}
}
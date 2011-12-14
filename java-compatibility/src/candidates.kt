namespace std

// TypeInfo
fun <T> typeinfo.TypeInfo<T>.getJavaClass() : java.lang.Class<T> { return (this as java.lang.Object).getClass() as Class<T> }
fun getJavaClass<T>() : java.lang.Class<T> { return typeinfo.typeinfo<T>.getJavaClass() }

//fun synchronized(lock : Any?, body : fun() : Unit) : Unit {}
//fun assert(condition : Boolean, message : fun() : String) : Unit {}

// String
fun <T> T.plus(str: String?) : String { return this.toString() + str }
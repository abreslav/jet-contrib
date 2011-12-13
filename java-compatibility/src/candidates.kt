namespace std

// TypeInfo
fun <T> typeinfo.TypeInfo<T>.getJavaClass() : java.lang.Class<T> { return (this as java.lang.Object).getClass() as Class<T> }
fun getJavaClass<T>() : java.lang.Class<T> { return typeinfo.typeinfo<T>.getJavaClass() }

//fun synchronized(lock : Any?, body : fun() : Unit) : Unit {}
//fun assert(condition : Boolean, message : fun() : String) : Unit {}

// Array "constructor"
fun <T> array(vararg t : T) : Array<T> = t

// "constructors" for primitive types array
fun doubleArray(vararg content : Double)    : DoubleArray   = content
fun floatArray(vararg content : Float)      : FloatArray    = content
fun longArray(vararg content : Long)        : LongArray     = content
fun intArray(vararg content : Int)          : IntArray      = content
fun charArray(vararg content : Char)        : CharArray     = content
fun shortArray(vararg content : Short)      : ShortArray    = content
fun byteArray(vararg content : Byte)        : ByteArray     = content
fun booleanArray(vararg content : Boolean)  : BooleanArray  = content

// some function for String
fun <T> T.plus(str: String?) : String { return this.toString() + str }
fun String.lastIndexOf(s: String) : Int { return (this as java.lang.String).lastIndexOf(s) }
fun String.lastIndexOf(s: Char) : Int { return (this as java.lang.String).lastIndexOf(s.toString()) }
fun String.indexOf(s : String) : Int { return (this as java.lang.String).indexOf(s) }
fun String.indexOf(p0 : String, p1 : Int) : Int { return (this as java.lang.String).indexOf(p0, p1) }
fun String.replaceAll(s: String, s1 : String) : String { return (this as java.lang.String).replaceAll(s, s1) as String }
fun String.trim() : String { return (this as java.lang.String).trim() as String }
fun String.toUpperCase() : String { return (this as java.lang.String).toUpperCase() as String }
fun String.length() : Int { return (this as java.lang.String).length() }
fun String.getBytes() : ByteArray { return (this as java.lang.String).getBytes() as ByteArray }
fun String.toCharArray() : CharArray { return (this as java.lang.String).toCharArray() as CharArray }
fun String.format(s : String, vararg objects : Any?) : String { return java.lang.String.format(s, objects) as String }
fun String.split(s : String) : Array<String> { return (this as java.lang.String).split(s) as Array<String> }
fun String.substring(i : Int) : String { return (this as java.lang.String).substring(i) as String }
fun String.substring(i0 : Int, i1 : Int) : String { return (this as java.lang.String).substring(i0, i1) as String }

// "constructors" for String
fun String(bytes : ByteArray?, i : Int, i1 : Int, s : String) : String = java.lang.String(bytes, i, i1, s) as String
fun String(bytes : ByteArray?, i : Int, i1 : Int, charset : java.nio.charset.Charset?) : String = java.lang.String(bytes, i, i1, charset) as String
fun String(bytes : ByteArray?, s : String?) : String = java.lang.String(bytes, s) as String
fun String(bytes : ByteArray?, charset : java.nio.charset.Charset?) : String = java.lang.String(bytes, charset) as String
fun String(bytes : ByteArray?, i : Int, i1 : Int) : String = java.lang.String(bytes, i, i1) as String
fun String(bytes : ByteArray?) : String = java.lang.String(bytes) as String
fun String(bytes : CharArray?) : String = java.lang.String(bytes) as String
fun String(stringBuffer : java.lang.StringBuffer?) : String = java.lang.String(stringBuffer) as String
fun String(stringBuilder : java.lang.StringBuilder?) : String = java.lang.String(stringBuilder) as String
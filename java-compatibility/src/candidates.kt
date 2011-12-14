namespace std.compatibility

// TypeInfo
fun <T> typeinfo.TypeInfo<T>.getJavaClass() : java.lang.Class<T> {
    return (this as java.lang.Object).getClass() as Class<T>
}
fun getJavaClass<T>() : java.lang.Class<T> {
    return typeinfo.typeinfo<T>.getJavaClass()
}

fun synchronized(lock : Any?, body : fun() : Unit) : Unit = body()

fun assert(condition : Boolean, message : fun() : String) = if (condition) System.err?.println("Assert: " + message())

// String
fun <T> T.plus(str: String) : String {
    return this.toString() + str
}

inline fun doubleArray(size : Int, init : fun(Int) : Double) : DoubleArray {
    val result = DoubleArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun floatArray(size : Int, init : fun(Int) : Float) : FloatArray {
    val result = FloatArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun longArray(size : Int, init : fun(Int) : Long) : LongArray {
    val result = LongArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun intArray(size : Int, init : fun(Int) : Int) : IntArray {
    val result = IntArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun charArray(size : Int, init : fun(Int) : Char) : CharArray {
    val result = CharArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun shortArray(size : Int, init : fun(Int) : Short) : ShortArray {
    val result = ShortArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun byteArray(size : Int, init : fun(Int) : Byte) : ByteArray {
    val result = ByteArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun booleanArray(size : Int, init : fun(Int) : Boolean) : BooleanArray {
    val result = BooleanArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun doubleArray(val size : Int) = DoubleArray(size)

inline fun floatArray(val size : Int) = FloatArray(size)

inline fun longArray(val size : Int) = LongArray(size)

inline fun intArray(val size : Int) = IntArray(size)

inline fun charArray(val size : Int) = CharArray(size)

inline fun shortArray(val size : Int) = ShortArray(size)

inline fun byteArray(val size : Int) = ByteArray(size)

inline fun booleanArray(val size : Int) = BooleanArray(size)
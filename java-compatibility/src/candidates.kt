package std.compatibility

// TypeInfo
fun <T> typeinfo.TypeInfo<T>.getJavaClass() : java.lang.Class<T> {
    return (this as java.lang.Object).getClass() as Class<T>
}
fun getJavaClass<T>() : java.lang.Class<T> {
    return typeinfo.typeinfo<T>.getJavaClass()
}

fun assert(condition : Boolean, message : () -> String) = if (condition) System.err?.println("Assert: " + message())

// Any
fun Any.getClass() = (this as java.lang.Object).getClass()

// String
inline fun <T> T.plus(str: String) = this.toString() + str

inline fun doubleArray(size : Int, init : (Int) -> Double) : DoubleArray {
    val result = DoubleArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun floatArray(size : Int, init : (Int) -> Float) : FloatArray {
    val result = FloatArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun longArray(size : Int, init : (Int) -> Long) : LongArray {
    val result = LongArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun intArray(size : Int, init : (Int) -> Int) : IntArray {
    val result = IntArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun charArray(size : Int, init : (Int) -> Char) : CharArray {
    val result = CharArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun shortArray(size : Int, init : (Int) -> Short) : ShortArray {
    val result = ShortArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun byteArray(size : Int, init : (Int) -> Byte) : ByteArray {
    val result = ByteArray(size)
    for (i in 0..size - 1)
        result[i] = init(i)
    return result
}

inline fun booleanArray(size : Int, init : (Int) -> Boolean) : BooleanArray {
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
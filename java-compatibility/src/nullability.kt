package std.compatibility

// Array
val <T> Array<T>?.length : Int get() = this.sure().size
//val <T> Array<T>?.length : Int get() = if (this!=null) this.size else throw NullPointerException()
fun <T> Array<T>?.get(i: Int) : T { if (this != null) return this.get(i) as T else throw NullPointerException() }
//fun <T> Array<T>?.get(i : Int?) = this.sure().get(i.sure())
fun <T> Array<T>?.set(i: Int, value: T) { if (this != null) return this.set(i, value) else throw NullPointerException() }

val DoubleArray?.length : Int get() = this.sure().size
val FloatArray?.length : Int get() = this.sure().size
val LongArray?.length : Int get() = this.sure().size
val IntArray?.length : Int get() = this.sure().size
val CharArray?.length : Int get() = this.sure().size
val ShortArray?.length : Int get() = this.sure().size
val ByteArray?.length : Int get() = this.sure().size
val BooleanArray?.length : Int get() = this.sure().size

// get for primitive types array
fun DoubleArray?.get(i : Int) = this.sure().get(i)
fun FloatArray?.get(i : Int) = this.sure().get(i)
fun LongArray?.get(i : Int) = this.sure().get(i)
fun IntArray?.get(i : Int) = this.sure().get(i)
fun CharArray?.get(i : Int) = this.sure().get(i)
fun ShortArray?.get(i : Int) = this.sure().get(i)
fun ByteArray?.get(i : Int) = this.sure().get(i)
fun BooleanArray?.get(i : Int) = this.sure().get(i)

// set for primitive types array
fun DoubleArray?.set(i : Int, v : Double) = this.sure().set(i, v)
fun FloatArray?.set(i : Int, v : Float) = this.sure().set(i, v)
fun LongArray?.set(i : Int, v : Long) = this.sure().set(i, v)
fun IntArray?.set(i : Int, v : Int) = this.sure().set(i, v)
fun CharArray?.set(i : Int, v : Char) = this.sure().set(i, v)
fun ShortArray?.set(i : Int, v : Short) = this.sure().set(i, v)
fun ByteArray?.set(i : Int, v : Byte) = this.sure().set(i, v)
fun BooleanArray?.set(i : Int, v : Boolean) = this.sure().set(i, v)

// "constructors" for boxed types
fun Double(content : Double) = java.lang.Double(content) as Double?
fun Float(content : Float) = java.lang.Float(content) as Float?
fun Long(content : Long) = java.lang.Long(content) as Long?
fun Int(content : Int) = java.lang.Integer(content) as Int?
fun Char(content : Char) = java.lang.Character(content) as Char?
fun Short(content : Short) = java.lang.Short(content) as Short?
fun Byte(content : Byte) = java.lang.Byte(content) as Byte?
fun Boolean(content : Boolean) = java.lang.Boolean(content) as Boolean?

fun Double(content : String) = java.lang.Double(content) as Double?
fun Float(content : String) = java.lang.Float(content) as Float?
fun Long(content : String) = java.lang.Long(content) as Long?
fun Int(content : String) = java.lang.Integer(content) as Int?
// fun Char(content : String) = java.lang.Character(content) as Char?
fun Short(content : String) = java.lang.Short(content) as Short?
fun Byte(content : String) = java.lang.Byte(content) as Byte?
fun Boolean(content : String) = java.lang.Boolean(content) as Boolean?

// Double  
fun Double?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Char?) = this.sure().compareTo(other.sure())

fun Double?.plus(other : Double?) = this.sure().plus(other.sure())
fun Double?.plus(other : Float?) = this.sure().plus(other.sure())
fun Double?.plus(other : Long?) = this.sure().plus(other.sure())
fun Double?.plus(other : Int?) = this.sure().plus(other.sure())
fun Double?.plus(other : Short?) = this.sure().plus(other.sure())
fun Double?.plus(other : Byte?) = this.sure().plus(other.sure())
fun Double?.plus(other : Char?) = this.sure().plus(other.sure())

fun Double?.minus(other : Double?) = this.sure().minus(other.sure())
fun Double?.minus(other : Float?) = this.sure().minus(other.sure())
fun Double?.minus(other : Long?) = this.sure().minus(other.sure())
fun Double?.minus(other : Int?) = this.sure().minus(other.sure())
fun Double?.minus(other : Short?) = this.sure().minus(other.sure())
fun Double?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Double?.minus(other : Char?) = this.sure().minus(other.sure())

fun Double?.times(other : Double?) = this.sure().times(other.sure())
fun Double?.times(other : Float?) = this.sure().times(other.sure())
fun Double?.times(other : Long?) = this.sure().times(other.sure())
fun Double?.times(other : Int?) = this.sure().times(other.sure())
fun Double?.times(other : Short?) = this.sure().times(other.sure())
fun Double?.times(other : Byte?) = this.sure().times(other.sure())
fun Double?.times(other : Char?) = this.sure().times(other.sure())

fun Double?.div(other : Double?) = this.sure().div(other.sure())
fun Double?.div(other : Float?) = this.sure().div(other.sure())
fun Double?.div(other : Long?) = this.sure().div(other.sure())
fun Double?.div(other : Int?) = this.sure().div(other.sure())
fun Double?.div(other : Short?) = this.sure().div(other.sure())
fun Double?.div(other : Byte?) = this.sure().div(other.sure())
fun Double?.div(other : Char?) = this.sure().div(other.sure())

fun Double?.mod(other : Double?) = this.sure().mod(other.sure())
fun Double?.mod(other : Float?) = this.sure().mod(other.sure())
fun Double?.mod(other : Long?) = this.sure().mod(other.sure())
fun Double?.mod(other : Int?) = this.sure().mod(other.sure())
fun Double?.mod(other : Short?) = this.sure().mod(other.sure())
fun Double?.mod(other : Byte?) = this.sure().mod(other.sure())

fun Double?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())

fun Double?.inc() = this.sure().inc()
fun Double?.dec() = this.sure().dec()
fun Double?.plus() = this.sure().plus()
fun Double?.minus() = this.sure().minus()

// Float
fun Float?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Char?) = this.sure().compareTo(other.sure())

fun Float?.plus(other : Double?) = this.sure().plus(other.sure())
fun Float?.plus(other : Float?) = this.sure().plus(other.sure())
fun Float?.plus(other : Long?) = this.sure().plus(other.sure())
fun Float?.plus(other : Int?) = this.sure().plus(other.sure())
fun Float?.plus(other : Short?) = this.sure().plus(other.sure())
fun Float?.plus(other : Byte?) = this.sure().plus(other.sure())
fun Float?.plus(other : Char?) = this.sure().plus(other.sure())

fun Float?.minus(other : Double?) = this.sure().minus(other.sure())
fun Float?.minus(other : Float?) = this.sure().minus(other.sure())
fun Float?.minus(other : Long?) = this.sure().minus(other.sure())
fun Float?.minus(other : Int?) = this.sure().minus(other.sure())
fun Float?.minus(other : Short?) = this.sure().minus(other.sure())
fun Float?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Float?.minus(other : Char?) = this.sure().minus(other.sure())

fun Float?.times(other : Double?) = this.sure().times(other.sure())
fun Float?.times(other : Float?) = this.sure().times(other.sure())
fun Float?.times(other : Long?) = this.sure().times(other.sure())
fun Float?.times(other : Int?) = this.sure().times(other.sure())
fun Float?.times(other : Short?) = this.sure().times(other.sure())
fun Float?.times(other : Byte?) = this.sure().times(other.sure())
fun Float?.times(other : Char?) = this.sure().times(other.sure())

fun Float?.div(other : Double?) = this.sure().div(other.sure())
fun Float?.div(other : Float?) = this.sure().div(other.sure())
fun Float?.div(other : Long?) = this.sure().div(other.sure())
fun Float?.div(other : Int?) = this.sure().div(other.sure())
fun Float?.div(other : Short?) = this.sure().div(other.sure())
fun Float?.div(other : Byte?) = this.sure().div(other.sure())
fun Float?.div(other : Char?) = this.sure().div(other.sure())

fun Float?.mod(other : Double?) = this.sure().mod(other.sure())
fun Float?.mod(other : Float?) = this.sure().mod(other.sure())
fun Float?.mod(other : Long?) = this.sure().mod(other.sure())
fun Float?.mod(other : Int?) = this.sure().mod(other.sure())
fun Float?.mod(other : Short?) = this.sure().mod(other.sure())
fun Float?.mod(other : Byte?) = this.sure().mod(other.sure())
fun Float?.mod(other : Char?) = this.sure().mod(other.sure())

fun Float?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())

fun Float?.inc() = this.sure().inc()
fun Float?.dec() = this.sure().dec()
fun Float?.plus() = this.sure().plus()
fun Float?.minus() = this.sure().minus()

// Long
fun Long?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Char?) = this.sure().compareTo(other.sure())

fun Long?.plus(other : Double?) = this.sure().plus(other.sure())
fun Long?.plus(other : Float?) = this.sure().plus(other.sure())
fun Long?.plus(other : Long?) = this.sure().plus(other.sure())
fun Long?.plus(other : Int?) = this.sure().plus(other.sure())
fun Long?.plus(other : Short?) = this.sure().plus(other.sure())
fun Long?.plus(other : Byte?) = this.sure().plus(other.sure())
fun Long?.plus(other : Char?) = this.sure().plus(other.sure())

fun Long?.minus(other : Double?) = this.sure().minus(other.sure())
fun Long?.minus(other : Float?) = this.sure().minus(other.sure())
fun Long?.minus(other : Long?) = this.sure().minus(other.sure())
fun Long?.minus(other : Int?) = this.sure().minus(other.sure())
fun Long?.minus(other : Short?) = this.sure().minus(other.sure())
fun Long?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Long?.minus(other : Char?) = this.sure().minus(other.sure())

fun Long?.times(other : Double?) = this.sure().times(other.sure())
fun Long?.times(other : Float?) = this.sure().times(other.sure())
fun Long?.times(other : Long?) = this.sure().times(other.sure())
fun Long?.times(other : Int?) = this.sure().times(other.sure())
fun Long?.times(other : Short?) = this.sure().times(other.sure())
fun Long?.times(other : Byte?) = this.sure().times(other.sure())
fun Long?.times(other : Char?) = this.sure().times(other.sure())

fun Long?.div(other : Double?) = this.sure().div(other.sure())
fun Long?.div(other : Float?) = this.sure().div(other.sure())
fun Long?.div(other : Long?) = this.sure().div(other.sure())
fun Long?.div(other : Int?) = this.sure().div(other.sure())
fun Long?.div(other : Short?) = this.sure().div(other.sure())
fun Long?.div(other : Byte?) = this.sure().div(other.sure())
fun Long?.div(other : Char?) = this.sure().div(other.sure())

fun Long?.mod(other : Double?) = this.sure().mod(other.sure())
fun Long?.mod(other : Float?) = this.sure().mod(other.sure())
fun Long?.mod(other : Long?) = this.sure().mod(other.sure())
fun Long?.mod(other : Int?) = this.sure().mod(other.sure())
fun Long?.mod(other : Short?) = this.sure().mod(other.sure())
fun Long?.mod(other : Byte?) = this.sure().mod(other.sure())
fun Long?.mod(other : Char?) = this.sure().mod(other.sure())

fun Long?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())

fun Long?.inc() = this.sure().inc()
fun Long?.dec() = this.sure().dec()
fun Long?.plus() = this.sure().plus()
fun Long?.minus() = this.sure().minus()

fun Long?.shl(bits : Int?) = this.sure().shl(bits.sure())
fun Long?.shr(bits : Int?) = this.sure().shr(bits.sure())
fun Long?.ushr(bits : Int?) = this.sure().ushr(bits.sure())
fun Long?.and(other : Long?) = this.sure().and(other.sure())
fun Long?.or(other : Long?) = this.sure().or(other.sure())
fun Long?.xor(other : Long?) = this.sure().xor(other.sure())
fun Long?.inv() = this.sure().inv()

// Int
fun Int?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Char?) = this.sure().compareTo(other.sure())

fun Int?.plus(other : Double?) = this.sure().plus(other.sure())
fun Int?.plus(other : Float?) = this.sure().plus(other.sure())
fun Int?.plus(other : Long?) = this.sure().plus(other.sure())
fun Int?.plus(other : Int?) = this.sure().plus(other.sure())
fun Int?.plus(other : Short?) = this.sure().plus(other.sure())
fun Int?.plus(other : Byte?) = this.sure().plus(other.sure())
fun Int?.plus(other : Char?) = this.sure().plus(other.sure())

fun Int?.minus(other : Double?) = this.sure().minus(other.sure())
fun Int?.minus(other : Float?) = this.sure().minus(other.sure())
fun Int?.minus(other : Long?) = this.sure().minus(other.sure())
fun Int?.minus(other : Int?) = this.sure().minus(other.sure())
fun Int?.minus(other : Short?) = this.sure().minus(other.sure())
fun Int?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Int?.minus(other : Char?) = this.sure().minus(other.sure())

fun Int?.times(other : Double?) = this.sure().times(other.sure())
fun Int?.times(other : Float?) = this.sure().times(other.sure())
fun Int?.times(other : Long?) = this.sure().times(other.sure())
fun Int?.times(other : Int?) = this.sure().times(other.sure())
fun Int?.times(other : Short?) = this.sure().times(other.sure())
fun Int?.times(other : Byte?) = this.sure().times(other.sure())
fun Int?.times(other : Char?) = this.sure().times(other.sure())

fun Int?.div(other : Double?) = this.sure().div(other.sure())
fun Int?.div(other : Float?) = this.sure().div(other.sure())
fun Int?.div(other : Long?) = this.sure().div(other.sure())
fun Int?.div(other : Int?) = this.sure().div(other.sure())
fun Int?.div(other : Short?) = this.sure().div(other.sure())
fun Int?.div(other : Byte?) = this.sure().div(other.sure())
fun Int?.div(other : Char?) = this.sure().div(other.sure())

fun Int?.mod(other : Double?) = this.sure().mod(other.sure())
fun Int?.mod(other : Float?) = this.sure().mod(other.sure())
fun Int?.mod(other : Long?) = this.sure().mod(other.sure())
fun Int?.mod(other : Int?) = this.sure().mod(other.sure())
fun Int?.mod(other : Short?) = this.sure().mod(other.sure())
fun Int?.mod(other : Byte?) = this.sure().mod(other.sure())
fun Int?.mod(other : Char?) = this.sure().mod(other.sure())

fun Int?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())

fun Int?.inc() = this.sure().inc()
fun Int?.dec() = this.sure().dec()
fun Int?.plus() = this.sure().plus()
fun Int?.minus() = this.sure().minus()

fun Int?.shl(bits : Int?) = this.sure().shl(bits.sure())
fun Int?.shr(bits : Int?) = this.sure().shr(bits.sure())
fun Int?.ushr(bits : Int?) = this.sure().ushr(bits.sure())
fun Int?.and(other : Int?) = this.sure().and(other.sure())
fun Int?.or(other : Int?) = this.sure().or(other.sure())
fun Int?.xor(other : Int?) = this.sure().xor(other.sure())
fun Int?.inv() = this.sure().inv()

// Char
fun Char?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Char?) = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())

fun Char?.plus(other : Double?) = this.sure().plus(other.sure())
fun Char?.plus(other : Float?) = this.sure().plus(other.sure())
fun Char?.plus(other : Long?) = this.sure().plus(other.sure())
fun Char?.plus(other : Int?) = this.sure().plus(other.sure())
fun Char?.plus(other : Short?) = this.sure().plus(other.sure())
fun Char?.plus(other : Byte?) = this.sure().plus(other.sure())

fun Char?.minus(other : Double?) = this.sure().minus(other.sure())
fun Char?.minus(other : Float?) = this.sure().minus(other.sure())
fun Char?.minus(other : Long?) = this.sure().minus(other.sure())
fun Char?.minus(other : Int?) = this.sure().minus(other.sure())
fun Char?.minus(other : Short?) = this.sure().minus(other.sure())
fun Char?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Char?.minus(other : Char?) = this.sure().minus(other.sure())

fun Char?.times(other : Double?) = this.sure().times(other.sure())
fun Char?.times(other : Float?) = this.sure().times(other.sure())
fun Char?.times(other : Long?) = this.sure().times(other.sure())
fun Char?.times(other : Int?) = this.sure().times(other.sure())
fun Char?.times(other : Short?) = this.sure().times(other.sure())
fun Char?.times(other : Byte?) = this.sure().times(other.sure())

fun Char?.div(other : Double?) = this.sure().div(other.sure())
fun Char?.div(other : Float?) = this.sure().div(other.sure())
fun Char?.div(other : Long?) = this.sure().div(other.sure())
fun Char?.div(other : Int?) = this.sure().div(other.sure())
fun Char?.div(other : Short?) = this.sure().div(other.sure())
fun Char?.div(other : Byte?) = this.sure().div(other.sure())

fun Char?.mod(other : Double?) = this.sure().mod(other.sure())
fun Char?.mod(other : Float?) = this.sure().mod(other.sure())
fun Char?.mod(other : Long?) = this.sure().mod(other.sure())
fun Char?.mod(other : Int?) = this.sure().mod(other.sure())
fun Char?.mod(other : Short?) = this.sure().mod(other.sure())
fun Char?.mod(other : Byte?) = this.sure().mod(other.sure())

fun Char?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())

fun Char?.inc() = this.sure().inc()
fun Char?.dec() = this.sure().dec()
fun Char?.plus() = this.sure().plus()
fun Char?.minus() = this.sure().minus()

// Short
fun Short?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Char?) = this.sure().compareTo(other.sure())

fun Short?.plus(other : Double?) = this.sure().plus(other.sure())
fun Short?.plus(other : Float?) = this.sure().plus(other.sure())
fun Short?.plus(other : Long?) = this.sure().plus(other.sure())
fun Short?.plus(other : Int?) = this.sure().plus(other.sure())
fun Short?.plus(other : Short?) = this.sure().plus(other.sure())
fun Short?.plus(other : Byte?) = this.sure().plus(other.sure())
fun Short?.plus(other : Char?) = this.sure().plus(other.sure())

fun Short?.minus(other : Double?) = this.sure().minus(other.sure())
fun Short?.minus(other : Float?) = this.sure().minus(other.sure())
fun Short?.minus(other : Long?) = this.sure().minus(other.sure())
fun Short?.minus(other : Int?) = this.sure().minus(other.sure())
fun Short?.minus(other : Short?) = this.sure().minus(other.sure())
fun Short?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Short?.minus(other : Char?) = this.sure().minus(other.sure())

fun Short?.times(other : Double?) = this.sure().times(other.sure())
fun Short?.times(other : Float?) = this.sure().times(other.sure())
fun Short?.times(other : Long?) = this.sure().times(other.sure())
fun Short?.times(other : Int?) = this.sure().times(other.sure())
fun Short?.times(other : Short?) = this.sure().times(other.sure())
fun Short?.times(other : Byte?) = this.sure().times(other.sure())
fun Short?.times(other : Char?) = this.sure().times(other.sure())

fun Short?.div(other : Double?) = this.sure().div(other.sure())
fun Short?.div(other : Float?) = this.sure().div(other.sure())
fun Short?.div(other : Long?) = this.sure().div(other.sure())
fun Short?.div(other : Int?) = this.sure().div(other.sure())
fun Short?.div(other : Short?) = this.sure().div(other.sure())
fun Short?.div(other : Byte?) = this.sure().div(other.sure())
fun Short?.div(other : Char?) = this.sure().div(other.sure())

fun Short?.mod(other : Double?) = this.sure().mod(other.sure())
fun Short?.mod(other : Float?) = this.sure().mod(other.sure())
fun Short?.mod(other : Long?) = this.sure().mod(other.sure())
fun Short?.mod(other : Int?) = this.sure().mod(other.sure())
fun Short?.mod(other : Short?) = this.sure().mod(other.sure())
fun Short?.mod(other : Byte?) = this.sure().mod(other.sure())
fun Short?.mod(other : Char?) = this.sure().mod(other.sure())

fun Short?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())

fun Short?.inc() = this.sure().inc()
fun Short?.dec() = this.sure().dec()
fun Short?.plus() = this.sure().plus()
fun Short?.minus() = this.sure().minus()

// Byte
fun Byte?.compareTo(other : Double?) = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Float?) = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Long?) = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Int?) = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Short?) = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Char?) = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Byte?) = this.sure().compareTo(other.sure())

fun Byte?.plus(other : Double?) = this.sure().plus(other.sure())
fun Byte?.plus(other : Float?) = this.sure().plus(other.sure())
fun Byte?.plus(other : Long?) = this.sure().plus(other.sure())
fun Byte?.plus(other : Int?) = this.sure().plus(other.sure())
fun Byte?.plus(other : Short?) = this.sure().plus(other.sure())
fun Byte?.plus(other : Byte?) = this.sure().plus(other.sure())
fun Byte?.plus(other : Char?) = this.sure().plus(other.sure())

fun Byte?.minus(other : Double?) = this.sure().minus(other.sure())
fun Byte?.minus(other : Float?) = this.sure().minus(other.sure())
fun Byte?.minus(other : Long?) = this.sure().minus(other.sure())
fun Byte?.minus(other : Int?) = this.sure().minus(other.sure())
fun Byte?.minus(other : Short?) = this.sure().minus(other.sure())
fun Byte?.minus(other : Byte?) = this.sure().minus(other.sure())
fun Byte?.minus(other : Char?) = this.sure().minus(other.sure())
 
fun Byte?.times(other : Double?) = this.sure().times(other.sure())
fun Byte?.times(other : Float?) = this.sure().times(other.sure())
fun Byte?.times(other : Long?) = this.sure().times(other.sure())
fun Byte?.times(other : Int?) = this.sure().times(other.sure())
fun Byte?.times(other : Short?) = this.sure().times(other.sure())
fun Byte?.times(other : Byte?) = this.sure().times(other.sure())
fun Byte?.times(other : Char?) = this.sure().times(other.sure())
 
fun Byte?.div(other : Double?) = this.sure().div(other.sure())
fun Byte?.div(other : Float?) = this.sure().div(other.sure())
fun Byte?.div(other : Long?) = this.sure().div(other.sure())
fun Byte?.div(other : Int?) = this.sure().div(other.sure())
fun Byte?.div(other : Short?) = this.sure().div(other.sure())
fun Byte?.div(other : Byte?) = this.sure().div(other.sure())
fun Byte?.div(other : Char?) = this.sure().div(other.sure())
 
fun Byte?.mod(other : Double?) = this.sure().mod(other.sure())
fun Byte?.mod(other : Float?) = this.sure().mod(other.sure())
fun Byte?.mod(other : Long?) = this.sure().mod(other.sure())
fun Byte?.mod(other : Int?) = this.sure().mod(other.sure())
fun Byte?.mod(other : Short?) = this.sure().mod(other.sure())
fun Byte?.mod(other : Byte?) = this.sure().mod(other.sure())
fun Byte?.mod(other : Char?) = this.sure().mod(other.sure())

fun Byte?.rangeTo(other : Double?) = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Float?) = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Long?) = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Int?) = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Short?) = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Byte?) = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Char?) = this.sure().rangeTo(other.sure())
 
fun Byte?.inc() = this.sure().inc()
fun Byte?.dec() = this.sure().dec()
fun Byte?.plus() = this.sure().plus()
fun Byte?.minus() = this.sure().minus()

// Boolean
fun Boolean?.not() = this.sure().not()
//fun Boolean?.and(other : Boolean?) = this.sure().and(other.sure())
//fun Boolean?.or(other : Boolean?) = this.sure().or(other.sure())
fun Boolean?.xor(other : Boolean?) = this.sure().xor(other.sure())

// Byte  
fun Byte.or(other : Int?) : Byte { return this.int.or(other).byt }
//fun Byte.or(other : Byte?) : Byte { return this.or(other).byt }
fun Byte.and(other : Int) = this.int.or(other).byt

// Long
fun Long.or(other : Byte)  = this.or(other.lng)
fun Long.or(other : Short) = this.or(other.lng)
fun Long.or(other : Int)   = this.or(other.lng)
fun Long.or(other : Char)  = this.or(other.lng)

fun Long.and(other : Byte)  = this.and(other.lng)
fun Long.and(other : Short) = this.and(other.lng)
fun Long.and(other : Int)   = this.and(other.lng)
fun Long.and(other : Char)  = this.and(other.lng)

// String
inline fun String.lastIndexOf(s: String?)  = (this as java.lang.String).lastIndexOf(s)

inline fun String.indexOf(s : String?) = (this as java.lang.String).indexOf(s)

inline fun String.indexOf(p0 : String?, p1 : Int) = (this as java.lang.String).indexOf(p0, p1)

inline fun String.replaceAll(s: String?, s1 : String?) = (this as java.lang.String).replaceAll(s, s1).sure()

inline fun String.split(s : String?)  = (this as java.lang.String).split(s)

inline fun String(bytes : ByteArray?, i : Int, i1 : Int, s : String?) = java.lang.String(bytes, i, i1, s) as String

inline fun String(bytes : ByteArray?, i : Int, i1 : Int, charset : java.nio.charset.Charset?) = java.lang.String(bytes, i, i1, charset) as String

inline fun String(bytes : ByteArray?, s : String?) = java.lang.String(bytes, s) as String

inline fun String(bytes : ByteArray?, charset : java.nio.charset.Charset?) = java.lang.String(bytes, charset) as String

inline fun String(bytes : ByteArray?, i : Int, i1 : Int) = java.lang.String(bytes, i, i1) as String

inline fun String(bytes : ByteArray?) = java.lang.String(bytes) as String

inline fun String(chars : CharArray?) = java.lang.String(chars) as String

inline fun String(stringBuffer : java.lang.StringBuffer?) = java.lang.String(stringBuffer) as String

inline fun String(stringBuilder : java.lang.StringBuilder?) = java.lang.String(stringBuilder) as String
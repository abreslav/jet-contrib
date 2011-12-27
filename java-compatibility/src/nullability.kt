package std.compatibility

// Array
val <T> Array<T>?.length : Int get() = this.sure().size
//val <T> Array<T>?.length : Int get() = if (this!=null) this.size else throw NullPointerException()
fun <T> Array<T>?.get(i: Int) : T { if (this != null) return this.get(i) as T else throw NullPointerException() }
//fun <T> Array<T>?.get(i : Int?) = this.sure().get(i.sure())
fun <T> Array<T>?.set(i: Int, value: T) { if (this != null) return this.set(i, value) else throw NullPointerException() }

// wired
//val DoubleArray?.length : Int get() = this.sure().size
//val FloatArray?.length : Int get() = this.sure().size
//val LongArray?.length : Int get() = this.sure().size
//val IntArray?.length : Int get() = this.sure().size
//val CharArray?.length : Int get() = this.sure().size
//val ShortArray?.length : Int get() = this.sure().size
//val ByteArray?.length : Int get() = this.sure().size
//val BooleanArray?.length : Int get() = this.sure().size

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
fun Double(content : Double) : Double? = java.lang.Double(content) as Double?
fun Float(content : Float) : Float? = java.lang.Float(content) as Float?
fun Long(content : Long) : Long? = java.lang.Long(content) as Long?
fun Int(content : Int) : Int? = java.lang.Integer(content) as Int?
fun Char(content : Char) : Char? = java.lang.Character(content) as Char?
fun Short(content : Short) : Short? = java.lang.Short(content) as Short?
fun Byte(content : Byte) : Byte? = java.lang.Byte(content) as Byte?
fun Boolean(content : Boolean) : Boolean? = java.lang.Boolean(content) as Boolean?

fun Double(content : String) : Double? = java.lang.Double(content) as Double?
fun Float(content : String) : Float? = java.lang.Float(content) as Float?
fun Long(content : String) : Long? = java.lang.Long(content) as Long?
fun Int(content : String) : Int? = java.lang.Integer(content) as Int?
// fun Char(content : String) : Char? = java.lang.Character(content) as Char?
fun Short(content : String) : Short? = java.lang.Short(content) as Short?
fun Byte(content : String) : Byte? = java.lang.Byte(content) as Byte?
fun Boolean(content : String) : Boolean? = java.lang.Boolean(content) as Boolean?

// Double  
fun Double?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())
fun Double?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())

fun Double?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Double?.plus(other : Float?) : Double = this.sure().plus(other.sure())
fun Double?.plus(other : Long?) : Double = this.sure().plus(other.sure())
fun Double?.plus(other : Int?) : Double = this.sure().plus(other.sure())
fun Double?.plus(other : Short?) : Double = this.sure().plus(other.sure())
fun Double?.plus(other : Byte?) : Double = this.sure().plus(other.sure())
fun Double?.plus(other : Char?) : Double = this.sure().plus(other.sure())

fun Double?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Double?.minus(other : Float?) : Double = this.sure().minus(other.sure())
fun Double?.minus(other : Long?) : Double = this.sure().minus(other.sure())
fun Double?.minus(other : Int?) : Double = this.sure().minus(other.sure())
fun Double?.minus(other : Short?) : Double = this.sure().minus(other.sure())
fun Double?.minus(other : Byte?) : Double = this.sure().minus(other.sure())
fun Double?.minus(other : Char?) : Double = this.sure().minus(other.sure())

fun Double?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Double?.times(other : Float?) : Double = this.sure().times(other.sure())
fun Double?.times(other : Long?) : Double = this.sure().times(other.sure())
fun Double?.times(other : Int?) : Double = this.sure().times(other.sure())
fun Double?.times(other : Short?) : Double = this.sure().times(other.sure())
fun Double?.times(other : Byte?) : Double = this.sure().times(other.sure())
fun Double?.times(other : Char?) : Double = this.sure().times(other.sure())

fun Double?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Double?.div(other : Float?) : Double = this.sure().div(other.sure())
fun Double?.div(other : Long?) : Double = this.sure().div(other.sure())
fun Double?.div(other : Int?) : Double = this.sure().div(other.sure())
fun Double?.div(other : Short?) : Double = this.sure().div(other.sure())
fun Double?.div(other : Byte?) : Double = this.sure().div(other.sure())
fun Double?.div(other : Char?) : Double = this.sure().div(other.sure())

fun Double?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Double?.mod(other : Float?) : Double = this.sure().mod(other.sure())
fun Double?.mod(other : Long?) : Double = this.sure().mod(other.sure())
fun Double?.mod(other : Int?) : Double = this.sure().mod(other.sure())
fun Double?.mod(other : Short?) : Double = this.sure().mod(other.sure())
fun Double?.mod(other : Byte?) : Double = this.sure().mod(other.sure())

fun Double?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Float?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Long?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Int?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Short?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Byte?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Double?.rangeTo(other : Char?) : Range<Double> = this.sure().rangeTo(other.sure())

fun Double?.inc() : Double = this.sure().inc()
fun Double?.dec() : Double = this.sure().dec()
fun Double?.plus() : Double = this.sure().plus()
fun Double?.minus() : Double = this.sure().minus()

// Float
fun Float?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())
fun Float?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())

fun Float?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Float?.plus(other : Float?) : Float = this.sure().plus(other.sure())
fun Float?.plus(other : Long?) : Float = this.sure().plus(other.sure())
fun Float?.plus(other : Int?) : Float = this.sure().plus(other.sure())
fun Float?.plus(other : Short?) : Float = this.sure().plus(other.sure())
fun Float?.plus(other : Byte?) : Float = this.sure().plus(other.sure())
fun Float?.plus(other : Char?) : Float = this.sure().plus(other.sure())

fun Float?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Float?.minus(other : Float?) : Float = this.sure().minus(other.sure())
fun Float?.minus(other : Long?) : Float = this.sure().minus(other.sure())
fun Float?.minus(other : Int?) : Float = this.sure().minus(other.sure())
fun Float?.minus(other : Short?) : Float = this.sure().minus(other.sure())
fun Float?.minus(other : Byte?) : Float = this.sure().minus(other.sure())
fun Float?.minus(other : Char?) : Float = this.sure().minus(other.sure())

fun Float?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Float?.times(other : Float?) : Float = this.sure().times(other.sure())
fun Float?.times(other : Long?) : Float = this.sure().times(other.sure())
fun Float?.times(other : Int?) : Float = this.sure().times(other.sure())
fun Float?.times(other : Short?) : Float = this.sure().times(other.sure())
fun Float?.times(other : Byte?) : Float = this.sure().times(other.sure())
fun Float?.times(other : Char?) : Float = this.sure().times(other.sure())

fun Float?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Float?.div(other : Float?) : Float = this.sure().div(other.sure())
fun Float?.div(other : Long?) : Float = this.sure().div(other.sure())
fun Float?.div(other : Int?) : Float = this.sure().div(other.sure())
fun Float?.div(other : Short?) : Float = this.sure().div(other.sure())
fun Float?.div(other : Byte?) : Float = this.sure().div(other.sure())
fun Float?.div(other : Char?) : Float = this.sure().div(other.sure())

fun Float?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Float?.mod(other : Float?) : Float = this.sure().mod(other.sure())
fun Float?.mod(other : Long?) : Float = this.sure().mod(other.sure())
fun Float?.mod(other : Int?) : Float = this.sure().mod(other.sure())
fun Float?.mod(other : Short?) : Float = this.sure().mod(other.sure())
fun Float?.mod(other : Byte?) : Float = this.sure().mod(other.sure())
fun Float?.mod(other : Char?) : Float = this.sure().mod(other.sure())

fun Float?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Float?) : Range<Float> = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Long?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Int?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Short?) : Range<Float> = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Byte?) : Range<Float> = this.sure().rangeTo(other.sure())
fun Float?.rangeTo(other : Char?) : Range<Float> = this.sure().rangeTo(other.sure())

fun Float?.inc() : Float = this.sure().inc()
fun Float?.dec() : Float = this.sure().dec()
fun Float?.plus() : Float = this.sure().plus()
fun Float?.minus() : Float = this.sure().minus()

// Long
fun Long?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())
fun Long?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())

fun Long?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Long?.plus(other : Float?) : Float = this.sure().plus(other.sure())
fun Long?.plus(other : Long?) : Long = this.sure().plus(other.sure())
fun Long?.plus(other : Int?) : Long = this.sure().plus(other.sure())
fun Long?.plus(other : Short?) : Long = this.sure().plus(other.sure())
fun Long?.plus(other : Byte?) : Long = this.sure().plus(other.sure())
fun Long?.plus(other : Char?) : Long = this.sure().plus(other.sure())

fun Long?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Long?.minus(other : Float?) : Float = this.sure().minus(other.sure())
fun Long?.minus(other : Long?) : Long = this.sure().minus(other.sure())
fun Long?.minus(other : Int?) : Long = this.sure().minus(other.sure())
fun Long?.minus(other : Short?) : Long = this.sure().minus(other.sure())
fun Long?.minus(other : Byte?) : Long = this.sure().minus(other.sure())
fun Long?.minus(other : Char?) : Long = this.sure().minus(other.sure())

fun Long?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Long?.times(other : Float?) : Float = this.sure().times(other.sure())
fun Long?.times(other : Long?) : Long = this.sure().times(other.sure())
fun Long?.times(other : Int?) : Long = this.sure().times(other.sure())
fun Long?.times(other : Short?) : Long = this.sure().times(other.sure())
fun Long?.times(other : Byte?) : Long = this.sure().times(other.sure())
fun Long?.times(other : Char?) : Long = this.sure().times(other.sure())

fun Long?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Long?.div(other : Float?) : Float = this.sure().div(other.sure())
fun Long?.div(other : Long?) : Long = this.sure().div(other.sure())
fun Long?.div(other : Int?) : Long = this.sure().div(other.sure())
fun Long?.div(other : Short?) : Long = this.sure().div(other.sure())
fun Long?.div(other : Byte?) : Long = this.sure().div(other.sure())
fun Long?.div(other : Char?) : Long = this.sure().div(other.sure())

fun Long?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Long?.mod(other : Float?) : Float = this.sure().mod(other.sure())
fun Long?.mod(other : Long?) : Long = this.sure().mod(other.sure())
fun Long?.mod(other : Int?) : Long = this.sure().mod(other.sure())
fun Long?.mod(other : Short?) : Long = this.sure().mod(other.sure())
fun Long?.mod(other : Byte?) : Long = this.sure().mod(other.sure())
fun Long?.mod(other : Char?) : Long = this.sure().mod(other.sure())

fun Long?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Float?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Long?) : LongRange = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Int?) : LongRange = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Short?) : LongRange = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Byte?) : LongRange = this.sure().rangeTo(other.sure())
fun Long?.rangeTo(other : Char?) : LongRange = this.sure().rangeTo(other.sure())

fun Long?.inc() : Long = this.sure().inc()
fun Long?.dec() : Long = this.sure().dec()
fun Long?.plus() : Long = this.sure().plus()
fun Long?.minus() : Long = this.sure().minus()

fun Long?.shl(bits : Int?) : Long = this.sure().shl(bits.sure())
fun Long?.shr(bits : Int?) : Long = this.sure().shr(bits.sure())
fun Long?.ushr(bits : Int?) : Long = this.sure().ushr(bits.sure())
fun Long?.and(other : Long?) : Long = this.sure().and(other.sure())
fun Long?.or(other : Long?) : Long = this.sure().or(other.sure())
fun Long?.xor(other : Long?) : Long = this.sure().xor(other.sure())
fun Long?.inv() : Long = this.sure().inv()

// Int
fun Int?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())
fun Int?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())

fun Int?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Int?.plus(other : Float?) : Float = this.sure().plus(other.sure())
fun Int?.plus(other : Long?) : Long = this.sure().plus(other.sure())
fun Int?.plus(other : Int?) = this.sure().plus(other.sure())
fun Int?.plus(other : Short?) : Int = this.sure().plus(other.sure())
fun Int?.plus(other : Byte?) : Int = this.sure().plus(other.sure())
fun Int?.plus(other : Char?) : Int = this.sure().plus(other.sure())

fun Int?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Int?.minus(other : Float?) : Float = this.sure().minus(other.sure())
fun Int?.minus(other : Long?) : Long = this.sure().minus(other.sure())
fun Int?.minus(other : Int?) : Int = this.sure().minus(other.sure())
fun Int?.minus(other : Short?) : Int = this.sure().minus(other.sure())
fun Int?.minus(other : Byte?) : Int = this.sure().minus(other.sure())
fun Int?.minus(other : Char?) : Int = this.sure().minus(other.sure())

fun Int?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Int?.times(other : Float?) : Float = this.sure().times(other.sure())
fun Int?.times(other : Long?) : Long = this.sure().times(other.sure())
fun Int?.times(other : Int?) : Int = this.sure().times(other.sure())
fun Int?.times(other : Short?) : Int = this.sure().times(other.sure())
fun Int?.times(other : Byte?) : Int = this.sure().times(other.sure())
fun Int?.times(other : Char?) : Int = this.sure().times(other.sure())

fun Int?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Int?.div(other : Float?) : Float = this.sure().div(other.sure())
fun Int?.div(other : Long?) : Long = this.sure().div(other.sure())
fun Int?.div(other : Int?) : Int = this.sure().div(other.sure())
fun Int?.div(other : Short?) : Int = this.sure().div(other.sure())
fun Int?.div(other : Byte?) : Int = this.sure().div(other.sure())
fun Int?.div(other : Char?) : Int = this.sure().div(other.sure())

fun Int?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Int?.mod(other : Float?) : Float = this.sure().mod(other.sure())
fun Int?.mod(other : Long?) : Long = this.sure().mod(other.sure())
fun Int?.mod(other : Int?) : Int = this.sure().mod(other.sure())
fun Int?.mod(other : Short?) : Int = this.sure().mod(other.sure())
fun Int?.mod(other : Byte?) : Int = this.sure().mod(other.sure())
fun Int?.mod(other : Char?) : Int = this.sure().mod(other.sure())

fun Int?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Float?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Long?) : LongRange = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Int?) : IntRange = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Short?) : IntRange = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Byte?) : IntRange = this.sure().rangeTo(other.sure())
fun Int?.rangeTo(other : Char?) : IntRange = this.sure().rangeTo(other.sure())

fun Int?.inc() : Int = this.sure().inc()
fun Int?.dec() : Int = this.sure().dec()
fun Int?.plus() : Int = this.sure().plus()
fun Int?.minus() : Int = this.sure().minus()

fun Int?.shl(bits : Int?) : Int = this.sure().shl(bits.sure())
fun Int?.shr(bits : Int?) : Int = this.sure().shr(bits.sure())
fun Int?.ushr(bits : Int?) : Int = this.sure().ushr(bits.sure())
fun Int?.and(other : Int?) : Int = this.sure().and(other.sure())
fun Int?.or(other : Int?) : Int = this.sure().or(other.sure())
fun Int?.xor(other : Int?) : Int = this.sure().xor(other.sure())
fun Int?.inv() : Int = this.sure().inv()

// Char
fun Char?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())
fun Char?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())

fun Char?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Char?.plus(other : Float?) : Float = this.sure().plus(other.sure())
fun Char?.plus(other : Long?) : Long = this.sure().plus(other.sure())
fun Char?.plus(other : Int?) : Int = this.sure().plus(other.sure())
fun Char?.plus(other : Short?) : Int = this.sure().plus(other.sure())
fun Char?.plus(other : Byte?) : Int = this.sure().plus(other.sure())

fun Char?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Char?.minus(other : Float?) : Float = this.sure().minus(other.sure())
fun Char?.minus(other : Long?) : Long = this.sure().minus(other.sure())
fun Char?.minus(other : Int?) : Int = this.sure().minus(other.sure())
fun Char?.minus(other : Short?) : Int = this.sure().minus(other.sure())
fun Char?.minus(other : Byte?) : Int = this.sure().minus(other.sure())
fun Char?.minus(other : Char?) : Int = this.sure().minus(other.sure())

fun Char?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Char?.times(other : Float?) : Float = this.sure().times(other.sure())
fun Char?.times(other : Long?) : Long = this.sure().times(other.sure())
fun Char?.times(other : Int?) : Int = this.sure().times(other.sure())
fun Char?.times(other : Short?) : Int = this.sure().times(other.sure())
fun Char?.times(other : Byte?) : Int = this.sure().times(other.sure())

fun Char?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Char?.div(other : Float?) : Float = this.sure().div(other.sure())
fun Char?.div(other : Long?) : Long = this.sure().div(other.sure())
fun Char?.div(other : Int?) : Int = this.sure().div(other.sure())
fun Char?.div(other : Short?) : Int = this.sure().div(other.sure())
fun Char?.div(other : Byte?) : Int = this.sure().div(other.sure())

fun Char?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Char?.mod(other : Float?) : Float = this.sure().mod(other.sure())
fun Char?.mod(other : Long?) : Long = this.sure().mod(other.sure())
fun Char?.mod(other : Int?) : Int = this.sure().mod(other.sure())
fun Char?.mod(other : Short?) : Int = this.sure().mod(other.sure())
fun Char?.mod(other : Byte?) : Int = this.sure().mod(other.sure())

fun Char?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Float?) : Range<Float> = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Long?) : LongRange = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Int?) : IntRange = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Short?) : IntRange = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Byte?) : IntRange = this.sure().rangeTo(other.sure())
fun Char?.rangeTo(other : Char?) : IntRange = this.sure().rangeTo(other.sure())

fun Char?.inc() : Char = this.sure().inc()
fun Char?.dec() : Char = this.sure().dec()
fun Char?.plus() : Int = this.sure().plus()
fun Char?.minus() : Int = this.sure().minus()

// Short
fun Short?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())
fun Short?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())

fun Short?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Short?.plus(other : Float?) : Float = this.sure().plus(other.sure())
fun Short?.plus(other : Long?) : Long = this.sure().plus(other.sure())
fun Short?.plus(other : Int?) : Int = this.sure().plus(other.sure())
fun Short?.plus(other : Short?) : Int = this.sure().plus(other.sure())
fun Short?.plus(other : Byte?) : Int = this.sure().plus(other.sure())
fun Short?.plus(other : Char?) : Int = this.sure().plus(other.sure())

fun Short?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Short?.minus(other : Float?) : Float = this.sure().minus(other.sure())
fun Short?.minus(other : Long?) : Long = this.sure().minus(other.sure())
fun Short?.minus(other : Int?) : Int = this.sure().minus(other.sure())
fun Short?.minus(other : Short?) : Int = this.sure().minus(other.sure())
fun Short?.minus(other : Byte?) : Int = this.sure().minus(other.sure())
fun Short?.minus(other : Char?) : Int = this.sure().minus(other.sure())

fun Short?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Short?.times(other : Float?) : Float = this.sure().times(other.sure())
fun Short?.times(other : Long?) : Long = this.sure().times(other.sure())
fun Short?.times(other : Int?) : Int = this.sure().times(other.sure())
fun Short?.times(other : Short?) : Int = this.sure().times(other.sure())
fun Short?.times(other : Byte?) : Int = this.sure().times(other.sure())
fun Short?.times(other : Char?) : Int = this.sure().times(other.sure())

fun Short?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Short?.div(other : Float?) : Float = this.sure().div(other.sure())
fun Short?.div(other : Long?) : Long = this.sure().div(other.sure())
fun Short?.div(other : Int?) : Int = this.sure().div(other.sure())
fun Short?.div(other : Short?) : Int = this.sure().div(other.sure())
fun Short?.div(other : Byte?) : Int = this.sure().div(other.sure())
fun Short?.div(other : Char?) : Int = this.sure().div(other.sure())

fun Short?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Short?.mod(other : Float?) : Float = this.sure().mod(other.sure())
fun Short?.mod(other : Long?) : Long = this.sure().mod(other.sure())
fun Short?.mod(other : Int?) : Int = this.sure().mod(other.sure())
fun Short?.mod(other : Short?) : Int = this.sure().mod(other.sure())
fun Short?.mod(other : Byte?) : Int = this.sure().mod(other.sure())
fun Short?.mod(other : Char?) : Int = this.sure().mod(other.sure())

fun Short?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Float?) : Range<Float> = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Long?) : LongRange = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Int?) : IntRange = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Short?) : IntRange = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Byte?) : IntRange = this.sure().rangeTo(other.sure())
fun Short?.rangeTo(other : Char?) : IntRange = this.sure().rangeTo(other.sure())

fun Short?.inc() : Short = this.sure().inc()
fun Short?.dec() : Short = this.sure().dec()
fun Short?.plus() : Short = this.sure().plus()
fun Short?.minus() : Short = this.sure().minus()

// Byte
fun Byte?.compareTo(other : Double?) : Int = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Float?) : Int = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Long?) : Int = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Int?) : Int = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Short?) : Int = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Char?) : Int = this.sure().compareTo(other.sure())
fun Byte?.compareTo(other : Byte?) : Int = this.sure().compareTo(other.sure())

fun Byte?.plus(other : Double?) : Double = this.sure().plus(other.sure())
fun Byte?.plus(other : Float?) : Float = this.sure().plus(other.sure())
fun Byte?.plus(other : Long?) : Long = this.sure().plus(other.sure())
fun Byte?.plus(other : Int?) : Int = this.sure().plus(other.sure())
fun Byte?.plus(other : Short?) : Int = this.sure().plus(other.sure())
fun Byte?.plus(other : Byte?) : Int = this.sure().plus(other.sure())
fun Byte?.plus(other : Char?) : Int = this.sure().plus(other.sure())

fun Byte?.minus(other : Double?) : Double = this.sure().minus(other.sure())
fun Byte?.minus(other : Float?) : Float = this.sure().minus(other.sure())
fun Byte?.minus(other : Long?) : Long = this.sure().minus(other.sure())
fun Byte?.minus(other : Int?) : Int = this.sure().minus(other.sure())
fun Byte?.minus(other : Short?) : Int = this.sure().minus(other.sure())
fun Byte?.minus(other : Byte?) : Int = this.sure().minus(other.sure())
fun Byte?.minus(other : Char?) : Int = this.sure().minus(other.sure())
 
fun Byte?.times(other : Double?) : Double = this.sure().times(other.sure())
fun Byte?.times(other : Float?) : Float = this.sure().times(other.sure())
fun Byte?.times(other : Long?) : Long = this.sure().times(other.sure())
fun Byte?.times(other : Int?) : Int = this.sure().times(other.sure())
fun Byte?.times(other : Short?) : Int = this.sure().times(other.sure())
fun Byte?.times(other : Byte?) : Int = this.sure().times(other.sure())
fun Byte?.times(other : Char?) : Int = this.sure().times(other.sure())
 
fun Byte?.div(other : Double?) : Double = this.sure().div(other.sure())
fun Byte?.div(other : Float?) : Float = this.sure().div(other.sure())
fun Byte?.div(other : Long?) : Long = this.sure().div(other.sure())
fun Byte?.div(other : Int?) : Int = this.sure().div(other.sure())
fun Byte?.div(other : Short?) : Int = this.sure().div(other.sure())
fun Byte?.div(other : Byte?) : Int = this.sure().div(other.sure())
fun Byte?.div(other : Char?) : Int = this.sure().div(other.sure())
 
fun Byte?.mod(other : Double?) : Double = this.sure().mod(other.sure())
fun Byte?.mod(other : Float?) : Float = this.sure().mod(other.sure())
fun Byte?.mod(other : Long?) : Long = this.sure().mod(other.sure())
fun Byte?.mod(other : Int?) : Int = this.sure().mod(other.sure())
fun Byte?.mod(other : Short?) : Int = this.sure().mod(other.sure())
fun Byte?.mod(other : Byte?) : Int = this.sure().mod(other.sure())
fun Byte?.mod(other : Char?) : Int = this.sure().mod(other.sure())

fun Byte?.rangeTo(other : Double?) : Range<Double> = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Float?) : Range<Float> = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Long?) : LongRange = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Int?) : IntRange = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Short?) : IntRange = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Byte?) : IntRange = this.sure().rangeTo(other.sure())
fun Byte?.rangeTo(other : Char?) : IntRange = this.sure().rangeTo(other.sure())
 
fun Byte?.inc() : Byte = this.sure().inc()
fun Byte?.dec() : Byte = this.sure().dec()
fun Byte?.plus() : Byte = this.sure().plus()
fun Byte?.minus() : Byte = this.sure().minus()

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
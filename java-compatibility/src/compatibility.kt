namespace std.compatibility
 
// TypeInfo  
fun <T> typeinfo.TypeInfo<T>.getJavaClass() : java.lang.Class<T> { return (this as java.lang.Object).getClass() as Class<T> }
fun getJavaClass<T>() : java.lang.Class<T> { return typeinfo.typeinfo<T>.getJavaClass() }
 
//fun synchronized(lock : Any?, body : fun() : Unit) : Unit = java.lang.String() as String
//fun assert(condition : Boolean, message : fun() : String) : Unit = java.lang.String() as String
 
// Array  
fun <T> array(vararg t : T) : Array<T> = t
val <T> Array<T>?.length : Int get() = this.sure().size
//val <T> Array<T>?.length : Int get() = if (this!=null) this.size else throw NullPointerException()
fun <T> Array<T>?.get(i: Int) : T { if (this != null) return this.get(i) as T else throw NullPointerException() }
fun <T> Array<T>?.set(i: Int, value: T) { if (this != null) return this.set(i, value) else throw NullPointerException() }

// "constructors" for primitive types array
fun DoubleArray(vararg content : Double) : DoubleArray = content
fun FloatArray(vararg content : Float) : FloatArray = content
fun LongArray(vararg content : Long) : LongArray = content
//fun IntArray(vararg content : Int) : IntArray = content
fun CharArray(vararg content : Char) : CharArray = content
fun ShortArray(vararg content : Short) : ShortArray = content
fun ByteArray(vararg content : Byte) : ByteArray = content
fun BooleanArray(vararg content : Boolean) : BooleanArray = content

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
fun Double?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Double?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Double?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Double?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Double?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Double?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Double?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Double?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Double?.plus(other : Float?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Double?.plus(other : Long?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Double?.plus(other : Int?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Double?.plus(other : Short?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Double?.plus(other : Byte?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Double?.plus(other : Char?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Double?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Double?.minus(other : Float?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Double?.minus(other : Long?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Double?.minus(other : Int?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Double?.minus(other : Short?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Double?.minus(other : Byte?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Double?.minus(other : Char?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }

fun Double?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Double?.times(other : Float?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Double?.times(other : Long?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Double?.times(other : Int?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Double?.times(other : Short?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Double?.times(other : Byte?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Double?.times(other : Char?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }

fun Double?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Double?.div(other : Float?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Double?.div(other : Long?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Double?.div(other : Int?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Double?.div(other : Short?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Double?.div(other : Byte?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Double?.div(other : Char?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }

fun Double?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Double?.mod(other : Float?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Double?.mod(other : Long?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Double?.mod(other : Int?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Double?.mod(other : Short?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Double?.mod(other : Byte?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }

fun Double?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Double?.rangeTo(other : Float?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Double?.rangeTo(other : Long?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Double?.rangeTo(other : Int?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Double?.rangeTo(other : Short?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Double?.rangeTo(other : Byte?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Double?.rangeTo(other : Char?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }

//fun Double?.inc() : Double { if (this != null) return this.inc() else throw NullPointerException() }
//fun Double?.dec() : Double { if (this != null) return this.dec() else throw NullPointerException() }
//fun Double?.plus() : Double { if (this != null) return this.plus() else throw NullPointerException() }
//fun Double?.minus() : Double { if (this != null) return this.minus() else throw NullPointerException() }

// Float
fun Float?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Float?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Float?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Float?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Float?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Float?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Float?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Float?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Float?.plus(other : Float?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Float?.plus(other : Long?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Float?.plus(other : Int?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Float?.plus(other : Short?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Float?.plus(other : Byte?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Float?.plus(other : Char?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Float?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Float?.minus(other : Float?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Float?.minus(other : Long?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Float?.minus(other : Int?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Float?.minus(other : Short?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Float?.minus(other : Byte?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Float?.minus(other : Char?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }

fun Float?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Float?.times(other : Float?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Float?.times(other : Long?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Float?.times(other : Int?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Float?.times(other : Short?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Float?.times(other : Byte?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Float?.times(other : Char?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }

fun Float?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Float?.div(other : Float?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Float?.div(other : Long?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Float?.div(other : Int?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Float?.div(other : Short?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Float?.div(other : Byte?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Float?.div(other : Char?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }

fun Float?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Float?.mod(other : Float?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Float?.mod(other : Long?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Float?.mod(other : Int?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Float?.mod(other : Short?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Float?.mod(other : Byte?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Float?.mod(other : Char?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }

fun Float?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Float?.rangeTo(other : Float?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Float?.rangeTo(other : Long?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Float?.rangeTo(other : Int?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Float?.rangeTo(other : Short?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Float?.rangeTo(other : Byte?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Float?.rangeTo(other : Char?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }

//fun Float?.inc() : Float { if (this != null) return this.inc() else throw NullPointerException() }
//fun Float?.dec() : Float { if (this != null) return this.dec() else throw NullPointerException() }
//fun Float?.plus() : Float { if (this != null) return this.plus() else throw NullPointerException() }
//fun Float?.minus() : Float { if (this != null) return this.minus() else throw NullPointerException() }

// Long
fun Long?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Long?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Long?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Long?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Long?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Long?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Long?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Long?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Long?.plus(other : Float?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Long?.plus(other : Long?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Long?.plus(other : Int?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Long?.plus(other : Short?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Long?.plus(other : Byte?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Long?.plus(other : Char?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Long?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Long?.minus(other : Float?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Long?.minus(other : Long?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Long?.minus(other : Int?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Long?.minus(other : Short?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Long?.minus(other : Byte?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Long?.minus(other : Char?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }

fun Long?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Long?.times(other : Float?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Long?.times(other : Long?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Long?.times(other : Int?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Long?.times(other : Short?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Long?.times(other : Byte?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Long?.times(other : Char?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }

fun Long?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Long?.div(other : Float?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Long?.div(other : Long?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Long?.div(other : Int?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Long?.div(other : Short?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Long?.div(other : Byte?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Long?.div(other : Char?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }

fun Long?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Long?.mod(other : Float?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Long?.mod(other : Long?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Long?.mod(other : Int?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Long?.mod(other : Short?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Long?.mod(other : Byte?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Long?.mod(other : Char?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }

fun Long?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Long?.rangeTo(other : Float?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Long?.rangeTo(other : Long?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Long?.rangeTo(other : Int?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Long?.rangeTo(other : Short?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Long?.rangeTo(other : Byte?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Long?.rangeTo(other : Char?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }

//fun Long?.inc() : Long { if (this != null) return this.inc() else throw NullPointerException() }
//fun Long?.dec() : Long { if (this != null) return this.dec() else throw NullPointerException() }
//fun Long?.plus() : Long { if (this != null) return this.plus() else throw NullPointerException() }
//fun Long?.minus() : Long { if (this != null) return this.minus() else throw NullPointerException() }

fun Long?.shl(bits : Int?) : Long { if (this != null) return this.shl(bits) else throw NullPointerException() }
fun Long?.shr(bits : Int?) : Long { if (this != null) return this.shr(bits) else throw NullPointerException() }
fun Long?.ushr(bits : Int?) : Long { if (this != null) return this.ushr(bits) else throw NullPointerException() }
fun Long?.and(other : Long?) : Long { if (this != null) return this.and(other) else throw NullPointerException() }
fun Long?.or(other : Long?) : Long { if (this != null) return this.or(other) else throw NullPointerException() }
fun Long?.xor(other : Long?) : Long { if (this != null) return this.xor(other) else throw NullPointerException() }
//fun Long?.inv() : Long { if (this != null) return this.inv() else throw NullPointerException() }

// Int
fun Int?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Int?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Int?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Int?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Int?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Int?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Int?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Int?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Int?.plus(other : Float?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Int?.plus(other : Long?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
//fun Int?.plus(other : Int?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Int?.plus(other : Int?) = this.sure().plus(other.sure())
fun Int?.plus(other : Short?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Int?.plus(other : Byte?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Int?.plus(other : Char?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Int?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Int?.minus(other : Float?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Int?.minus(other : Long?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Int?.minus(other : Int?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Int?.minus(other : Short?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Int?.minus(other : Byte?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Int?.minus(other : Char?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }

fun Int?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Int?.times(other : Float?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Int?.times(other : Long?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Int?.times(other : Int?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Int?.times(other : Short?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Int?.times(other : Byte?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Int?.times(other : Char?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }

fun Int?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Int?.div(other : Float?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Int?.div(other : Long?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Int?.div(other : Int?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Int?.div(other : Short?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Int?.div(other : Byte?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Int?.div(other : Char?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }

fun Int?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Int?.mod(other : Float?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Int?.mod(other : Long?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Int?.mod(other : Int?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Int?.mod(other : Short?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Int?.mod(other : Byte?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Int?.mod(other : Char?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }

fun Int?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Int?.rangeTo(other : Float?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Int?.rangeTo(other : Long?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Int?.rangeTo(other : Int?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Int?.rangeTo(other : Short?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Int?.rangeTo(other : Byte?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Int?.rangeTo(other : Char?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }

//fun Int?.inc() : Int { if (this != null) return this.inc() else throw NullPointerException() }
//fun Int?.dec() : Int { if (this != null) return this.dec() else throw NullPointerException() }
//fun Int?.plus() : Int { if (this != null) return this.plus() else throw NullPointerException() }
//fun Int?.minus() : Int { if (this != null) return this.minus() else throw NullPointerException() }

fun Int?.shl(bits : Int?) : Int { if (this != null) return this.shl(bits) else throw NullPointerException() }
fun Int?.shr(bits : Int?) : Int { if (this != null) return this.shr(bits) else throw NullPointerException() }
fun Int?.ushr(bits : Int?) : Int { if (this != null) return this.ushr(bits) else throw NullPointerException() }
fun Int?.and(other : Int?) : Int { if (this != null) return this.and(other) else throw NullPointerException() }
fun Int?.or(other : Int?) : Int { if (this != null) return this.or(other) else throw NullPointerException() }
fun Int?.xor(other : Int?) : Int { if (this != null) return this.xor(other) else throw NullPointerException() }
//fun Int?.inv() : Int { if (this != null) return this.inv() else throw NullPointerException() }

// Char
fun Char?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Char?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Char?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Char?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Char?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Char?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Char?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Char?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Char?.plus(other : Float?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Char?.plus(other : Long?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Char?.plus(other : Int?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Char?.plus(other : Short?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Char?.plus(other : Byte?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Char?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Char?.minus(other : Float?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Char?.minus(other : Long?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Char?.minus(other : Int?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Char?.minus(other : Short?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Char?.minus(other : Byte?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Char?.minus(other : Char?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }

fun Char?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Char?.times(other : Float?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Char?.times(other : Long?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Char?.times(other : Int?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Char?.times(other : Short?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Char?.times(other : Byte?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }

fun Char?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Char?.div(other : Float?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Char?.div(other : Long?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Char?.div(other : Int?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Char?.div(other : Short?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Char?.div(other : Byte?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }

fun Char?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Char?.mod(other : Float?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Char?.mod(other : Long?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Char?.mod(other : Int?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Char?.mod(other : Short?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Char?.mod(other : Byte?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }

fun Char?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Char?.rangeTo(other : Float?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Char?.rangeTo(other : Long?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Char?.rangeTo(other : Int?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Char?.rangeTo(other : Short?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Char?.rangeTo(other : Byte?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Char?.rangeTo(other : Char?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }

//fun Char?.inc() : Char { if (this != null) return this.inc() else throw NullPointerException() }
//fun Char?.dec() : Char { if (this != null) return this.dec() else throw NullPointerException() }
//fun Char?.plus() : Int { if (this != null) return this.plus() else throw NullPointerException() }
//fun Char?.minus() : Int { if (this != null) return this.minus() else throw NullPointerException() }

// Short
fun Short?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Short?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Short?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Short?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Short?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Short?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Short?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Short?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Short?.plus(other : Float?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Short?.plus(other : Long?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Short?.plus(other : Int?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Short?.plus(other : Short?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Short?.plus(other : Byte?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Short?.plus(other : Char?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Short?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Short?.minus(other : Float?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Short?.minus(other : Long?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Short?.minus(other : Int?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Short?.minus(other : Short?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Short?.minus(other : Byte?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Short?.minus(other : Char?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }

fun Short?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Short?.times(other : Float?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Short?.times(other : Long?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Short?.times(other : Int?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Short?.times(other : Short?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Short?.times(other : Byte?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Short?.times(other : Char?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }

fun Short?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Short?.div(other : Float?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Short?.div(other : Long?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Short?.div(other : Int?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Short?.div(other : Short?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Short?.div(other : Byte?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Short?.div(other : Char?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }

fun Short?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Short?.mod(other : Float?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Short?.mod(other : Long?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Short?.mod(other : Int?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Short?.mod(other : Short?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Short?.mod(other : Byte?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Short?.mod(other : Char?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }

fun Short?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Short?.rangeTo(other : Float?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Short?.rangeTo(other : Long?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Short?.rangeTo(other : Int?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Short?.rangeTo(other : Short?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Short?.rangeTo(other : Byte?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Short?.rangeTo(other : Char?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }

//fun Short?.inc() : Short { if (this != null) return this.inc() else throw NullPointerException() }
//fun Short?.dec() : Short { if (this != null) return this.dec() else throw NullPointerException() }
//fun Short?.plus() : Short { if (this != null) return this.plus() else throw NullPointerException() }
//fun Short?.minus() : Short { if (this != null) return this.minus() else throw NullPointerException() }

// Byte
fun Byte?.compareTo(other : Double?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Byte?.compareTo(other : Float?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Byte?.compareTo(other : Long?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Byte?.compareTo(other : Int?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Byte?.compareTo(other : Short?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Byte?.compareTo(other : Char?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }
fun Byte?.compareTo(other : Byte?) : Int { if (this != null) return this.compareTo(other) else throw NullPointerException() }

fun Byte?.plus(other : Double?) : Double { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Byte?.plus(other : Float?) : Float { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Byte?.plus(other : Long?) : Long { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Byte?.plus(other : Int?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Byte?.plus(other : Short?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Byte?.plus(other : Byte?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }
fun Byte?.plus(other : Char?) : Int { if (this != null) return this.plus(other) else throw NullPointerException() }

fun Byte?.minus(other : Double?) : Double { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Byte?.minus(other : Float?) : Float { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Byte?.minus(other : Long?) : Long { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Byte?.minus(other : Int?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Byte?.minus(other : Short?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Byte?.minus(other : Byte?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
fun Byte?.minus(other : Char?) : Int { if (this != null) return this.minus(other) else throw NullPointerException() }
 
fun Byte?.times(other : Double?) : Double { if (this != null) return this.times(other) else throw NullPointerException() }
fun Byte?.times(other : Float?) : Float { if (this != null) return this.times(other) else throw NullPointerException() }
fun Byte?.times(other : Long?) : Long { if (this != null) return this.times(other) else throw NullPointerException() }
fun Byte?.times(other : Int?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Byte?.times(other : Short?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Byte?.times(other : Byte?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
fun Byte?.times(other : Char?) : Int { if (this != null) return this.times(other) else throw NullPointerException() }
 
fun Byte?.div(other : Double?) : Double { if (this != null) return this.div(other) else throw NullPointerException() }
fun Byte?.div(other : Float?) : Float { if (this != null) return this.div(other) else throw NullPointerException() }
fun Byte?.div(other : Long?) : Long { if (this != null) return this.div(other) else throw NullPointerException() }
fun Byte?.div(other : Int?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Byte?.div(other : Short?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Byte?.div(other : Byte?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
fun Byte?.div(other : Char?) : Int { if (this != null) return this.div(other) else throw NullPointerException() }
 
fun Byte?.mod(other : Double?) : Double { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Byte?.mod(other : Float?) : Float { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Byte?.mod(other : Long?) : Long { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Byte?.mod(other : Int?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Byte?.mod(other : Short?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Byte?.mod(other : Byte?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
fun Byte?.mod(other : Char?) : Int { if (this != null) return this.mod(other) else throw NullPointerException() }
 
fun Byte?.rangeTo(other : Double?) : Range<Double> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Byte?.rangeTo(other : Float?) : Range<Float> { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Byte?.rangeTo(other : Long?) : LongRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Byte?.rangeTo(other : Int?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Byte?.rangeTo(other : Short?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Byte?.rangeTo(other : Byte?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
fun Byte?.rangeTo(other : Char?) : IntRange { if (this != null) return this.rangeTo(other) else throw NullPointerException() }
 
//fun Byte?.inc() : Byte { if (this != null) return this.inc() else throw NullPointerException() }
//fun Byte?.dec() : Byte { if (this != null) return this.dec() else throw NullPointerException() }
//fun Byte?.plus() : Byte { if (this != null) return this.plus() else throw NullPointerException() }
//fun Byte?.minus() : Byte { if (this != null) return this.minus() else throw NullPointerException() }
 
// Byte  
fun Byte.or(other : Int?) : Byte { return this.int.or(other).byt }
fun Byte.or(other : Byte?) : Byte { return this.or(other).byt }
 
// String  
fun <T> T.plus(str: String?) : String { return this.toString() + str }
fun String.lastIndexOf(s: String) : Int { return (this as java.lang.String).lastIndexOf(s) }
fun String.lastIndexOf(s: Char) : Int { return (this as java.lang.String).lastIndexOf(s.toString()) }
fun String.indexOf(p0 : String, p1 : Int) : Int { return (this as java.lang.String).indexOf(p0, p1) }
fun String.replaceAll(s: String, s1 : String) : String { return (this as java.lang.String).replaceAll(s, s1) as String }
fun String.trim() : String { return (this as java.lang.String).trim() as String }
fun String.length() : Int { return (this as java.lang.String).length() }
fun String.getBytes() : ByteArray { return (this as java.lang.String).getBytes() as ByteArray }
fun String.format(s : String, vararg objects : Any?) : String { return java.lang.String.format(s, objects) as String }
 
// "constructors" for String
fun String(bytes : ByteArray?, i : Int, i1 : Int, s : String) : String = java.lang.String(bytes, i, i1) as String
fun String(bytes : ByteArray?, i : Int, i1 : Int, charset : java.nio.charset.Charset?) : String = java.lang.String(bytes, i, i1, charset) as String
fun String(bytes : ByteArray?, s : String?) : String = java.lang.String(bytes, s) as String
fun String(bytes : ByteArray?, charset : java.nio.charset.Charset?) : String = java.lang.String(bytes, charset) as String
fun String(bytes : ByteArray?, i : Int, i1 : Int) : String = java.lang.String(bytes, i, i1) as String
fun String(bytes : ByteArray?) : String = java.lang.String(bytes) as String
fun String(stringBuffer : java.lang.StringBuffer?) : String = java.lang.String(stringBuffer) as String
fun String(stringBuilder : java.lang.StringBuilder?) : String = java.lang.String(stringBuilder) as String

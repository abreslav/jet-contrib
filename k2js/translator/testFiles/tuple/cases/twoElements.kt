package foo

fun box() : Boolean {
    val a : #(Int, Int) = #(1, 2)
    return ((a._1 == 1) && (a._2 == 2))
 }
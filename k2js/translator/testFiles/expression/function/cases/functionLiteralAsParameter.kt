namespace foo

fun apply(f : fun(Int) : Int, t : Int) : Int {
    return f(t)
}


fun box() : Boolean {
  return apply({(a: Int) => a + 5 }, 3) == 8
}
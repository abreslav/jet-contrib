namespace foo

class MyInt(i : Int) {
  var b = i
    fun inc() : MyInt {
      b = b++;
      return this;
    }
}

fun box() : Boolean {
  var t = MyInt(0)
  t++;
  return (t.b == 0)
}
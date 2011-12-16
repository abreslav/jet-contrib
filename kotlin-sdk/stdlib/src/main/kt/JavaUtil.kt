namespace std

namespace util {
    import java.util.*

  /*
  TODO These seem already defined, but not available for use?
  val String.size : Int
      get() = this.length()

  val String.empty : Boolean
      get() = length == 0

  TODO are these baked in somewhere too?
  val Array<*>.size : Int
      get() = this.length

  val Array<*>.empty : Boolean
      get() = size == 0
  */



    /** Creates a new list */
    fun list<T>(vararg values: T) : ArrayList<T> {
      val answer = ArrayList<T>()
      for (v in values) {
        answer.add(v)
      }
      return answer;
    }

    val Collection<*>.size : Int
        get() = size()

    val Collection<*>.empty : Boolean
        get() = isEmpty()

    fun <T> java.lang.Iterable<T>.any(predicate: fun(T): Boolean) : Boolean {
      for (elem in this) {
        if (predicate(elem)) {
          return true
        }
      }
      return false
    }

    fun <T> java.lang.Iterable<T>.all(predicate: fun(T): Boolean) : Boolean {
      for (elem in this) {
        if (!predicate(elem)) {
          return false
        }
      }
      return true
    }

    /** Returns the first item in the collection which matches the given predicate or null if none matched */
    fun <T> java.lang.Iterable<T>.find(predicate: fun(T): Boolean) : T? {
      for (elem in this) {
        if (predicate(elem))
          return elem
      }
      return null
    }

    fun <T> java.lang.Iterable<T>.filter(predicate: fun(T): Boolean) : List<T> {
      val result = ArrayList<T>()
      for (elem in this) {
        if (predicate(elem))
          result.add(elem)
      }
      return result
    }

    fun <T, R> java.lang.Iterable<T>.map(transform : fun(T) : R) : List<R> {
      val result = ArrayList<R>()
      for (item in this)
        result.add(transform(item))
      return result
    }
}
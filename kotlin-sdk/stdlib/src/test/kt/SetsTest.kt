namespace test.collections

import std.io.*
import std.util.*
import std.testing.*
import java.util.*

class SetsTest() : TestSupport() {
  val data = hashSet("foo", "bar")

  fun testAny() {
    assert {
      data.any{it.startsWith("f")}
    }
    assertNot {
      data.any{it.startsWith("x")}
    }
  }

  fun testAll() {
    assert {
      data.all{it.length == 3}
    }
    assertNot {
      data.all{s => s.startsWith("b")}
    }
  }

  fun testFilter() {
    val foo = data.filter{it.startsWith("f")}

    assert {
      foo.all{it.startsWith("f")}
    }
    assertEquals(1, foo.size)
    assertEquals(arrayList("foo"), foo)

    // TODO ideally foo would now be a set
    assert("Filter on a Set should return a Set") {
      foo is Set<String>
    }
  }

  fun testFind() {
    val x = data.find{it.startsWith("x")}
    assertNull(x)
    /*
    TODO compiler bug

    fails {
      x.sure()
    }
    */

    val f = data.find{it.startsWith("f")}
    f.sure()
    assertEquals("foo", f)
  }

  fun testMap() {
    /**
      TODO compiler bug
      we should be able to remove the explicit type on the function
      http://youtrack.jetbrains.net/issue/KT-849
    */
    val lengths = data.map<String,Int>{s => s.length}
    assert {
      lengths.all{it == 3}
    }
    assertEquals(2, lengths.size)
    assertEquals(arrayList(3, 3), lengths)
  }

}
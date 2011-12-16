namespace test.collections

import std.io.*
import std.util.*
import std.testing.*
import java.util.*

class CollectionsTest() : TestSupport() {
  val data = list("foo", "bar")

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
    assertEquals(list("foo"), foo)
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

  /*
  TODO compiler bug :)
  http://youtrack.jetbrains.net/issue/KT-849

  fun testMap() {
    val lengths = data.map{s => s.length}
    println("Lengths are ${lengths}")
    assert {
      lengths.all{it == 3}
    }
    assertEquals(3, lengths.size)
    assertEquals(list(3, 3), lengths)
  }
  */

}
namespace std

namespace testing {

  import std.io.*
  import std.util.*
  import java.util.*

  import org.junit.*
  import org.junit.runner.*
  import org.junit.runner.notification.*
  import junit.framework.*

  fun assert(message: String, block: fun() : Boolean) {
    val actual = block()
    Assert.assertTrue(message, actual)
  }

  fun assert(block: fun() : Boolean) = assert(block.toString(), block)

  fun assertNot(message: String, block: fun() : Boolean) {
    assert(message){ !block() }
  }

  fun assertNot(block: fun() : Boolean) = assertNot(block.toString(), block)

  fun assert(actual: Boolean, message: String) {
    println("Answer: ${actual} for ${message}")
  }

  fun assertEquals(expected: Any, actual: Any?, message: String = "") {
    Assert.assertEquals(message, expected, actual)
  }

  fun assertNull(actual: Any?, message: String = "") {
    Assert.assertNull(message, actual)
  }

  fun fails(block: fun() : Any) {
    try {
      block()
      Assert.fail("Expected an exception to be thrown")
    } catch (e: Exception) {
      // OK
    }
  }


  /*
  TODO we could maybe create our own test runner for JUnit
  to avoid a runtime dependency on JUnit for running tests?

  class KotlinTestRunner() : Runner() {

    override fun getDescription(): Description? {
      return null
    }

    override fun run(notifier: RunNotifier?) {
      println("About to run a test case on ${this}")
    }
  }
  */

  // TODO no annotations yet?
  //@RunWith(KotlinTestRunner)
  abstract class TestSupport() : TestCase() {

  }
}

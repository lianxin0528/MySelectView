package space.lianxin.myselcetview

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun test() {
    var items = arrayListOf("A","S","F","G","Z")
    for (i in 1..items.size){
      println(i)
    }
  }

}

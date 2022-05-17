package id.muhammadfaisal.formulirapp

import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun task2(): Int {
        val n = 15958

        var a = ""
        a + "c"
        var maxValue = Int.MIN_VALUE
        val stringValue = n.toString()
        for (i in stringValue.indices) {
            if (stringValue[i] == '5') {
                val stringToCheck = stringValue.substring(0, i) + stringValue.substring(i + 1)
                val intToCheck = stringToCheck.toInt()
                maxValue = if (intToCheck > maxValue) intToCheck else maxValue
            }
        }
        return maxValue
    }

    @Test
    fun task3() {
        val number = 10
        val max = 0
        for (i in 0 until number) {
            i.inc() + 1
        }
    }

}
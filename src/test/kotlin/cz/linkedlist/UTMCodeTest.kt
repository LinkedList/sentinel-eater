package cz.linkedlist

import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`


/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
class UTMCodeTest {
    @Test
    @Throws(Exception::class)
    fun testToString() {
        val code = UTMCode(36, "M", "TD")
        assertThat(code.toString(), `is`("36/M/TD/"))
    }

    @Test
    @Throws(Exception::class)
    fun testOf() {
        val code = UTMCode.of("36MTD")
        assertThat(code.toString(), `is`("36/M/TD/"))
    }

    @Test
    @Throws(Exception::class)
    fun testOfWithSlashes() {
        val code = UTMCode.of("36/M/TD/")
        assertThat(code.toString(), `is`("36/M/TD/"))
    }

    @Test
    @Throws(Exception::class)
    fun testOfWithLeadingZero() {
        val code = UTMCode.of("06MTD")
        assertThat(code.toString(), `is`("6/M/TD/"))
    }

}
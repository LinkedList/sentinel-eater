package cz.linkedlist


import com.amazonaws.services.s3.model.S3ObjectSummary
import org.junit.Test

import java.time.LocalDate
import java.util.ArrayList

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasItem

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
class DateParserTest {

    @Test
    @Throws(Exception::class)
    fun testParse() {
        val obj = S3ObjectSummary()
        obj.key = "tiles/36/M/TD/2017/1/15/0/B01.jp2"
        val objects = ArrayList<S3ObjectSummary>()
        objects.add(obj)

        val dates = DateParser.parseS3(objects)
        assertThat(dates, hasItem(LocalDate.of(2017, 1, 15)))
    }

    @Test
    @Throws(Exception::class)
    fun testParseNoDate() {
        val obj = S3ObjectSummary()
        obj.key = "tiles/36/M/TD/random.txt"
        val objects = ArrayList<S3ObjectSummary>()
        objects.add(obj)

        val dates = DateParser.parseS3(objects)
        assertThat(dates, empty())
    }
}
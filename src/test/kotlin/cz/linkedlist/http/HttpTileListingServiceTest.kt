package cz.linkedlist.http

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.sun.org.apache.xpath.internal.operations.Bool
import cz.linkedlist.TileSet
import cz.linkedlist.UTMCode
import cz.linkedlist.cache.Cache
import org.junit.Before
import org.junit.Test
import org.springframework.web.client.RestTemplate

import java.time.LocalDate
import java.util.Optional

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.mockito.Matchers.anyObject
import org.mockito.Mockito
import java.time.chrono.ChronoLocalDate

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
class HttpTileListingServiceTest {

    private var service: HttpTileListingService? = null

    @Before
    fun init() {
        val cache = mock<Cache> {
            on { exists(any())} doReturn Optional.empty()
            on { insert(any(), eq(true))} doReturn true
            on { insert(any(), eq(false))} doReturn false
        }
        service = HttpTileListingService(RestTemplate(), cache)
    }

    @Test
    @Throws(Exception::class)
    fun testExists() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        assertThat(service!!.exists(tileSet), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun testNotExists() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31), 3)
        assertThat(service!!.exists(tileSet), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun testGetYears() {
        val code = UTMCode.of("36MTD")
        val years = service!!.getYears(code)
        assertThat(years, hasSize(3))
        assertThat(years, hasItem(2015))
        assertThat(years, hasItem(2016))
        assertThat(years, hasItem(2017))
    }

    @Test
    @Throws(Exception::class)
    fun testGetMonths() {
        val code = UTMCode.of("36MTD")
        val months = service!!.getMonths(code, 2015)
        assertThat(months, hasSize(5))
        assertThat(months, hasItem(10))
        assertThat(months, hasItem(11))
        assertThat(months, hasItem(12))
        assertThat(months, hasItem(7))
        assertThat(months, hasItem(9))
    }

    @Test
    @Throws(Exception::class)
    fun testGetDays() {
        val code = UTMCode.of("36MTD")
        val days = service!!.getDays(code, 2015, 10)
        assertThat(days, hasSize(2))
        assertThat(days, hasItem(23))
        assertThat(days, hasItem(3))
    }

    @Test
    @Throws(Exception::class)
    fun testGetDatasets() {
        val code = UTMCode.of("36MTD")
        val dataSets = service!!.getDataSets(code, 2015, 10, 23)
        assertThat(dataSets, hasSize(1))
        assertThat(dataSets, hasItem(0))
    }

    @Test
    @Throws(Exception::class)
    fun testGetFolderContents() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2015, 10, 23))
        val folderContents = service!!.getFolderContents(tileSet)

        assertAllBands(folderContents)
        assertThat(folderContents, hasItem("tiles/36/M/TD/2015/10/23/0/metadata.xml"))
        assertThat(folderContents, hasItem("tiles/36/M/TD/2015/10/23/0/productInfo.json"))
        assertThat(folderContents, hasItem("tiles/36/M/TD/2015/10/23/0/tileInfo.json"))
    }

    @Test
    @Throws(Exception::class)
    fun testAvailableDates() {
        val code = UTMCode.of("20MMN")
        val localDates = service!!.availableDates(code)
        assertThat(localDates, hasSize(1))
        assertThat(localDates, hasItem(LocalDate.of(2015, 12, 6)))
    }

    @Test
    @Throws(Exception::class)
    fun testAvailableDatesAfter() {
        val code = UTMCode.of("20MMN")
        val localDates = service!!.availableDatesAfter(code, LocalDate.of(2014, 1, 6))
        assertThat(localDates, hasSize(1))
        assertThat(localDates, hasItem(LocalDate.of(2015, 12, 6)))
    }

    @Test
    @Throws(Exception::class)
    fun testAvailableDatesAfterWithoutAvailableDates() {
        val code = UTMCode.of("20MMN")
        val localDates = service!!.availableDatesAfter(code, LocalDate.of(2017, 1, 6))
        assertThat(localDates, hasSize(0))
    }

    @Test
    @Throws(Exception::class)
    fun testAvailableDatesAfterWithDateInTheSameYearAndMonth() {
        val code = UTMCode.of("20MMN")
        val localDates = service!!.availableDatesAfter(code, LocalDate.of(2015, 12, 5))
        assertThat(localDates, hasSize(1))
        assertThat(localDates, hasItem(LocalDate.of(2015, 12, 6)))
    }

    @Test
    @Throws(Exception::class)
    fun testAvailableDatesAfterAreSortedAscendingly() {
        val localDates = service!!.availableDatesAfter(UTMCode.of("36MTD"), LocalDate.of(2017, 1, 1))
        var previous: LocalDate? = null
        for (localDate in localDates) {
            if (previous == null) {
                previous = localDate
                continue
            }
            assertThat(previous, lessThan<ChronoLocalDate>(localDate))
            previous = localDate
        }
    }

    private fun assertAllBands(contents: List<String>) {
        for (i in 1..12) {
            assertThat(contents, hasItem("tiles/36/M/TD/2015/10/23/0/B" + String.format("%02d", i) + ".jp2"))
        }
        assertThat(contents, hasItem("tiles/36/M/TD/2015/10/23/0/B8A.jp2"))
    }
}

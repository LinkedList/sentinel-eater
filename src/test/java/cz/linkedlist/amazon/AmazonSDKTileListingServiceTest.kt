package cz.linkedlist.amazon

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsV2Request
import com.amazonaws.services.s3.model.ListObjectsV2Result
import com.amazonaws.services.s3.model.S3ObjectSummary
import cz.linkedlist.SentinelEater
import cz.linkedlist.TileListingService
import cz.linkedlist.TileSet
import cz.linkedlist.UTMCode
import cz.linkedlist.cache.Cache
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import java.time.LocalDate
import java.util.Arrays
import java.util.Optional

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.mockito.Matchers.any
import org.mockito.Matchers.anyObject
import org.mockito.Matchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
class AmazonSDKTileListingServiceTest {

    private var service: TileListingService? = null
    private var cache: Cache? = null

    @Before
    fun init() {
        cache = Mockito.mock(Cache::class.java)
        `when`(cache!!.exists(anyObject())).thenReturn(Optional.empty())
        `when`(cache!!.insert(anyObject(), eq(true))).thenReturn(true)
        `when`(cache!!.insert(anyObject(), eq(false))).thenReturn(false)
    }

    @Test
    @Throws(Exception::class)
    fun testGetYears() {
        val client = mock(AmazonS3Client::class.java)
        val code = UTMCode(10, "M", "AB")
        val list = ListObjectsV2Result()
        list.commonPrefixes = Arrays.asList(
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2016/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2017/"
        )
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        val years = service!!.getYears(code)

        assertThat(years, hasSize(3))
        assertThat(years, hasItem(2015))
        assertThat(years, hasItem(2016))
        assertThat(years, hasItem(2017))
    }

    @Test
    @Throws(Exception::class)
    fun testGetMonths() {
        val client = mock(AmazonS3Client::class.java)
        val code = UTMCode(10, "M", "AB")
        val list = ListObjectsV2Result()
        list.commonPrefixes = Arrays.asList(
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/1/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/2/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/3/"
        )
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        val months = service!!.getMonths(code, 2015)

        assertThat(months, hasSize(3))
        assertThat(months, hasItem(1))
        assertThat(months, hasItem(2))
        assertThat(months, hasItem(3))
    }

    @Test
    @Throws(Exception::class)
    fun testGetDays() {
        val client = mock(AmazonS3Client::class.java)
        val code = UTMCode(10, "M", "AB")
        val list = ListObjectsV2Result()
        list.commonPrefixes = Arrays.asList(
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/1/15/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/1/18/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/1/25/"
        )
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        val days = service!!.getDays(code, 2015, 1)

        assertThat(days, hasSize(3))
        assertThat(days, hasItem(15))
        assertThat(days, hasItem(18))
        assertThat(days, hasItem(25))
    }

    @Test
    @Throws(Exception::class)
    fun testGetDataSets() {
        val client = mock(AmazonS3Client::class.java)
        val code = UTMCode(10, "M", "AB")
        val list = ListObjectsV2Result()
        list.commonPrefixes = Arrays.asList(
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/1/15/0/",
            SentinelEater.INSTANCE.getTILES() + code.toString() + "2015/1/15/1/"
        )
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        val dataSets = service!!.getDataSets(code, 2015, 1, 15)

        assertThat(dataSets, hasSize(2))
        assertThat(dataSets, hasItem(0))
        assertThat(dataSets, hasItem(1))

    }

    @Test
    @Throws(Exception::class)
    fun testExists() {
        val client = mock(AmazonS3Client::class.java)
        val tileSet = TileSet(UTMCode(36, "M", "TD"), LocalDate.of(2016, 8, 31), 1)
        val list = ListObjectsV2Result()
        list.keyCount = 1
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        assertThat(service!!.exists(tileSet), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun testGetFolderContents() {
        val client = mock(AmazonS3Client::class.java)
        val tileSet = TileSet(UTMCode(36, "M", "TD"), LocalDate.of(2016, 8, 31), 1)
        val list = ListObjectsV2Result()
        list.objectSummaries.add(summary("tiles/33/U/XQ/2016/8/31/0/B01.jp2"))
        list.objectSummaries.add(summary("tiles/33/U/XQ/2016/8/31/0/B02.jp2"))
        list.objectSummaries.add(summary("tiles/33/U/XQ/2016/8/31/0/B03.jp2"))
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        assertThat(service!!.getFolderContents(tileSet), hasItem("tiles/33/U/XQ/2016/8/31/0/B01.jp2"))
        assertThat(service!!.getFolderContents(tileSet), hasItem("tiles/33/U/XQ/2016/8/31/0/B02.jp2"))
        assertThat(service!!.getFolderContents(tileSet), hasItem("tiles/33/U/XQ/2016/8/31/0/B03.jp2"))
    }

    @Test
    @Throws(Exception::class)
    fun testNotExists() {
        val client = mock(AmazonS3Client::class.java)
        val tileSet = TileSet(UTMCode(36, "M", "TD"), LocalDate.of(2016, 8, 31), 1)
        val list = ListObjectsV2Result()
        list.keyCount = 0
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)

        service = AmazonSDKTileListingService(client, cache!!)

        assertThat(service!!.exists(tileSet), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun testAvailableDatesUtmCode() {
        val client = mock(AmazonS3Client::class.java)
        val code = UTMCode(33, "U", "XQ")
        val list = ListObjectsV2Result()
        list.objectSummaries.add(summary("tiles/33/U/XQ/2016/8/31/0/B01.jp2"))
        list.objectSummaries.add(summary("tiles/33/U/XQ/2017/8/30/0/B01.jp2"))
        list.objectSummaries.add(summary("tiles/33/U/XQ/2017/2/14/0/B01.jp2"))
        `when`(client.listObjectsV2(any(ListObjectsV2Request::class.java))).thenReturn(list)
        service = AmazonSDKTileListingService(client, cache!!)

        val dates = service!!.availableDates(code)
        assertThat(dates, hasSize(3))
        assertThat(dates, hasItem(LocalDate.of(2016, 8, 31)))
        assertThat(dates, hasItem(LocalDate.of(2017, 8, 30)))
        assertThat(dates, hasItem(LocalDate.of(2017, 2, 14)))
    }

    private fun summary(key: String): S3ObjectSummary {
        val summary = S3ObjectSummary()
        summary.key = key
        return summary
    }
}

package cz.linkedlist.http

import cz.linkedlist.TileListingService
import cz.linkedlist.TileSet
import cz.linkedlist.UTMCode
import cz.linkedlist.cache.Cache
import org.junit.Before
import org.junit.Test
import org.springframework.web.client.RestTemplate

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.util.Arrays
import java.util.Optional

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertTrue
import org.mockito.Matchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.File

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
class HttpTileDownloaderTest {

    private var listingService: HttpTileListingService? = null
    private var downloader: HttpTileDownloader? = null

    @Before
    fun init() {
        val cache = mock(Cache::class.java)
        `when`(cache.exists(anyObject())).thenReturn(Optional.empty())
        `when`(cache.insert(anyObject(), eq(true))).thenReturn(true)
        `when`(cache.insert(anyObject(), eq(false))).thenReturn(false)
        listingService = HttpTileListingService(RestTemplate(), cache)
        downloader = HttpTileDownloader(listingService!!)
        downloader!!.destinationFolder = "/tmp/"
    }

    @Test
    @Throws(Exception::class)
    fun testDownloadProductInfo() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"))
        Files.deleteIfExists(path)
        downloader!!.downProductInfo(tileSet)
        assertThat(Files.exists(path), `is`(true))
        assertTrue(Files.size(path) > 0)
        Files.deleteIfExists(path)
    }

    @Test
    @Throws(Exception::class)
    fun testDownloadTileInfo() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.tileInfo().replace("/", "_"))
        Files.deleteIfExists(path)
        downloader!!.downTileInfo(tileSet)
        assertThat(Files.exists(path), `is`(true))
        assertTrue(Files.size(path) > 0)
        Files.deleteIfExists(path)
    }

    @Test
    @Throws(Exception::class)
    fun testMetadata() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.metadata().replace("/", "_"))
        Files.deleteIfExists(path)
        downloader!!.downMetadata(tileSet)
        assertThat(Files.exists(path), `is`(true))
        assertTrue(Files.size(path) > 0)
        Files.deleteIfExists(path)
    }

    @Test
    @Throws(Exception::class)
    fun testBand() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.band(1).get().replace("/", "_"))
        Files.deleteIfExists(path)
        downloader!!.downBand(tileSet, 1)
        assertThat(Files.exists(path), `is`(true))
        assertTrue(Files.size(path) > 0)
        Files.deleteIfExists(path)
    }

    @Test
    @Throws(Exception::class)
    fun testDownContent() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.band(1).get().replace("/", "_"))
        Files.deleteIfExists(path)
        downloader!!.downContent(tileSet, TileSet.Contents.BAND_1)
        assertThat(Files.exists(path), `is`(true))
        assertTrue(Files.size(path) > 0)
        Files.deleteIfExists(path)
    }

    @Test
    @Throws(Exception::class)
    fun testDownContents() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path1 = Paths.get("/tmp/" + tileSet.band(1).get().replace("/", "_"))
        val path2 = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"))
        Files.deleteIfExists(path1)
        Files.deleteIfExists(path2)
        downloader!!.downContent(tileSet, arrayOf(TileSet.Contents.BAND_1, TileSet.Contents.PROD_INFO))
        assertThat(Files.exists(path1), `is`(true))
        assertTrue(Files.size(path1) > 0)
        Files.deleteIfExists(path1)

        assertThat(Files.exists(path2), `is`(true))
        assertTrue(Files.size(path2) > 0)
        Files.deleteIfExists(path2)
    }

    @Test(expected = RuntimeException::class)
    fun testNotExists() {
        val listingService = mock(TileListingService::class.java)
        `when`(listingService.exists(any())).thenReturn(false)

        val downloader = HttpTileDownloader(listingService)

        val tileSet = TileSet(UTMCode.of("36AAA"), LocalDate.of(2016, 8, 31))
        downloader.down(tileSet, tileSet.productInfo())
    }

    @Test
    @Throws(Exception::class)
    fun testNotDownloaded() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"))
        Files.deleteIfExists(path)
        assertThat<File>(downloader!!.isDownloaded(tileSet.productInfo().replace("/", "_")), `is`(nullValue()))
        downloader!!.downProductInfo(tileSet)
        assertThat<File>(downloader!!.isDownloaded(tileSet.productInfo().replace("/", "_")), `is`<File>(path.toFile()))
        Files.deleteIfExists(path)
    }

    @Test
    @Throws(Exception::class)
    fun testIsDownloaded() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"))
        Files.deleteIfExists(path)
        downloader!!.downProductInfo(tileSet)
        assertThat<File>(downloader!!.isDownloaded(tileSet.productInfo().replace("/", "_")), `is`<File>(path.toFile()))
        downloader!!.downProductInfo(tileSet)
        Files.deleteIfExists(path)
    }
}
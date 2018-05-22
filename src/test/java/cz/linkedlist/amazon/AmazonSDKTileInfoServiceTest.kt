package cz.linkedlist.amazon

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.fasterxml.jackson.databind.ObjectMapper
import cz.linkedlist.*
import cz.linkedlist.config.JacksonConfiguration
import cz.linkedlist.info.ProductInfo
import cz.linkedlist.info.TileInfo
import org.apache.http.client.methods.HttpRequestBase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.util.concurrent.ListenableFuture

import java.time.LocalDate
import java.util.Arrays

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.mockito.Matchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
class AmazonSDKTileInfoServiceTest {

    @Mock
    private val listingService: TileListingService? = null
    @Mock
    private val client: AmazonS3Client? = null
    private val downInfoService = DownInfoServiceImpl()

    private var service: AmazonSDKTileInfoService? = null

    private var productInfoResource: Resource? = null
    private var tileInfoResource: Resource? = null

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        val classLoader = javaClass.classLoader
        productInfoResource = UrlResource(classLoader.getResource("productInfo.json")!!)
        tileInfoResource = UrlResource(classLoader.getResource("tileInfo.json")!!)

        service = AmazonSDKTileInfoService()
        ReflectionTestUtils.setField(downInfoService, "service", service)
        ReflectionTestUtils.setField(service, "downInfoService", downInfoService)
        ReflectionTestUtils.setField(service, "client", client)
        ReflectionTestUtils.setField(service, "listingService", listingService)
        val jacksonConfiguration = JacksonConfiguration()
        val jackson2ObjectMapperBuilder = jacksonConfiguration.jackson2ObjectMapperBuilder()
        val mapper = jackson2ObjectMapperBuilder.build<ObjectMapper>()
        ReflectionTestUtils.setField(service, "mapper", mapper)
    }

    @Test(expected = RuntimeException::class)
    fun testGetProductInfoNotExists() {
        `when`(listingService!!.exists(any())).thenReturn(false)
        service!!.getTileInfo(TileSet.of("36MTD", 2016, 8, 31))
    }

    @Test
    @Throws(Exception::class)
    fun testGetProductInfo() {
        `when`(listingService!!.exists(any())).thenReturn(true)
        val set = TileSet.of("36MTD", 2016, 8, 31)
        val `object` = mock(S3Object::class.java)
        `when`(`object`.objectContent).thenReturn(
            S3ObjectInputStream(
                productInfoResource!!.inputStream,
                mock(HttpRequestBase::class.java)
            )
        )

        `when`(client!!.getObject(any<GetObjectRequest>())).thenReturn(`object`)
        val productInfo = service!!.getProductInfo(set)
        assertThat(productInfo, not(nullValue()))
        assertThat(productInfo.getId(), not<T>(nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun testGetTileInfo() {
        `when`(listingService!!.exists(any())).thenReturn(true)
        val set = TileSet.of("36MTD", 2016, 8, 31)
        val `object` = mock(S3Object::class.java)
        `when`(`object`.objectContent).thenReturn(
            S3ObjectInputStream(
                tileInfoResource!!.inputStream,
                mock(HttpRequestBase::class.java)
            )
        )

        `when`(client!!.getObject(any<GetObjectRequest>())).thenReturn(`object`)
        val tileInfo = service!!.getTileInfo(set)
        assertThat(tileInfo, not(nullValue()))
        assertThat<String>(tileInfo.path, not(nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun testDownloadTileInfoAndSetToTileSets() {
        `when`(listingService!!.exists(any())).thenReturn(true)
        val `object` = mock(S3Object::class.java)
        `when`(`object`.objectContent).thenReturn(
            S3ObjectInputStream(tileInfoResource!!.inputStream, mock(HttpRequestBase::class.java)),
            S3ObjectInputStream(tileInfoResource!!.inputStream, mock(HttpRequestBase::class.java))
        )
        `when`(client!!.getObject(any<GetObjectRequest>())).thenReturn(`object`)
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val tileSet2 = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val f = service!!.downTileInfo(Arrays.asList(tileSet, tileSet2))
        f.addCallback(
            { list ->
                assertThat(list, hasSize(2))
                list.forEach { set ->
                    assertThat<TileInfo>(set.info, not(nullValue()))
                    assertThat<Double>(set.cloudiness(), closeTo(87.73, 0.0))
                }
            }) { t -> Assert.fail() }
    }

    @Test
    @Throws(Exception::class)
    fun testDownloadTileInfo() {
        `when`(listingService!!.exists(any())).thenReturn(true)
        val `object` = mock(S3Object::class.java)
        `when`(`object`.objectContent).thenReturn(
            S3ObjectInputStream(tileInfoResource!!.inputStream, mock(HttpRequestBase::class.java))
        )
        `when`(client!!.getObject(any<GetObjectRequest>())).thenReturn(`object`)
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val f = service!!.downTileInfo(tileSet)
        f.addCallback(
            { set ->
                assertThat<TileInfo>(set.info, not(nullValue()))
                assertThat<Double>(set.cloudiness(), closeTo(87.73, 0.0))
            }) { t -> Assert.fail() }
    }

    @Test
    fun testKey() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        assertThat(AmazonSDKTileInfoService.key(tileSet, TileInfo::class.java), `is`(tileSet.tileInfo()))
        assertThat(AmazonSDKTileInfoService.key(tileSet, ProductInfo::class.java), `is`(tileSet.productInfo()))
    }

    @Test(expected = RuntimeException::class)
    fun testKeyNotExistentClass() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        AmazonSDKTileInfoService.key(tileSet, Any::class.java)
    }
}
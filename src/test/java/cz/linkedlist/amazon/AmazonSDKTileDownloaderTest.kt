package cz.linkedlist.amazon

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import cz.linkedlist.TileListingService
import cz.linkedlist.TileSet
import cz.linkedlist.UTMCode
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpRequestBase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate

import org.assertj.core.api.Assertions.assertThat
import org.mockito.Matchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@RunWith(MockitoJUnitRunner::class)
class AmazonSDKTileDownloaderTest {

    @Mock
    private val listingService: TileListingService? = null
    @Mock
    private val client: AmazonS3Client? = null
    internal var downloader: AmazonSDKTileDownloader

    @Before
    fun init() {
        downloader = AmazonSDKTileDownloader(client!!, listingService!!)
    }

    @Test(expected = RuntimeException::class)
    fun testNotExists() {
        `when`(listingService!!.exists(any())).thenReturn(false)

        val tileSet = TileSet(UTMCode.of("36AAA"), LocalDate.of(2016, 8, 31))
        downloader.down(tileSet, tileSet.productInfo())
    }

    @Test
    @Throws(Exception::class)
    fun testDownload() {
        `when`(listingService!!.exists(any())).thenReturn(true)
        val mockReturn = mock(S3Object::class.java)
        `when`(client!!.getObject(any(GetObjectRequest::class.java))).thenReturn(mockReturn)
        `when`(mockReturn.objectContent).thenReturn(
            S3ObjectInputStream(
                IOUtils.toInputStream("test"),
                mock(HttpRequestBase::class.java)
            )
        )
        downloader.destinationFolder = "/tmp/testFolder/"
        val tileSet = TileSet(UTMCode.of("36AAA"), LocalDate.of(2016, 8, 31))
        downloader.down(tileSet, tileSet.productInfo())
        val downloadedFile = Paths.get("/tmp/testFolder/" + tileSet.productInfo().replace("/", "_"))
        assertThat(Files.exists(downloadedFile))
        //tests already downloaded file
        downloader.down(tileSet, tileSet.productInfo())
        Files.delete(downloadedFile)
        Files.delete(Paths.get("/tmp/testFolder"))
    }

}
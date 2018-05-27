package cz.linkedlist

import cz.linkedlist.http.HttpTileDownloader
import org.junit.After
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.mockito.Mockito.mock

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
class TileDownloaderTest {

    private val downloader = HttpTileDownloader(mock(TileListingService::class.java))

    @After
    @Throws(Exception::class)
    fun after() {
        val destFolder = Paths.get("/tmp/testFolder")
        if (Files.exists(destFolder)) {
            Files.delete(destFolder)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testEnsureFolderExistsReturns() {
        val destFolder = "/tmp/testFolder"
        Files.createDirectory(Paths.get(destFolder))
        downloader.ensureFolderExists(destFolder)
        assertThat(Files.exists(Paths.get(destFolder)), `is`(true))
        assertThat(Files.isDirectory(Paths.get(destFolder)), `is`(true))
        Files.delete(Paths.get(destFolder))
    }

    @Test
    @Throws(Exception::class)
    fun testEnsureFolderExistsCreates() {
        val destFolder = "/tmp/testFolder"
        downloader.ensureFolderExists(destFolder)
        assertThat(Files.exists(Paths.get(destFolder)), `is`(true))
        assertThat(Files.isDirectory(Paths.get(destFolder)), `is`(true))
        Files.delete(Paths.get(destFolder))
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun testEnsureFolderNotExists() {
        val destFolder = "/tmp/testFolder"
        Files.createFile(Paths.get(destFolder))
        downloader.ensureFolderExists(destFolder)
        Files.delete(Paths.get(destFolder))
    }
}
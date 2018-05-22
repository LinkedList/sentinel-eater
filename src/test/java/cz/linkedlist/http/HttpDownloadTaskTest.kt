package cz.linkedlist.http

import cz.linkedlist.*
import cz.linkedlist.info.TileInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.concurrent.SuccessCallback

import java.time.LocalDate
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@SpringBootTest
@RunWith(SpringRunner::class)
class HttpDownloadTaskTest {

    @Autowired
    private val infoService: TileInfoService? = null

    @Autowired
    private val listingService: TileListingService? = null

    @Autowired
    private val tileDownloader: TileDownloader? = null

    @Test
    fun testRun() {
        val downloadTask = DownloadTask(UTMCode.of("36MTD"), 20.0, LocalDate.of(2016, 5, 1), emptyList<Any>())
        val task = HttpDownloadTask(downloadTask, listingService, infoService, tileDownloader)
        task.run()
    }

    @Test
    fun testSuccess() {
        val set1 = TileSet.of("36MTD", 2016, 5, 1)
        val set2 = TileSet.of("36MTD", 2016, 5, 2)
        val info1 = TileInfo()
        info1.setCloudyPixelPercentage(50.0)

        val info2 = TileInfo()
        info2.setCloudyPixelPercentage(80.0)

        set1.info = info1
        set2.info = info2

        val task = DownloadTask(UTMCode.of("36MTD"), 70.0, LocalDate.of(2016, 5, 1), emptyList<Any>())
        val successResultTileSets = ArrayList<TileSet>()
        val success = HttpDownloadTask.HttpDownloadTaskSuccess(task, successResultTileSets)

        success.onSuccess(Arrays.asList(set1, set2))

        assertThat<List<TileSet>>(successResultTileSets, hasSize(1))
        assertThat<List<TileSet>>(successResultTileSets, hasItem(set1))
    }
}
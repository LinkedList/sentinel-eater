package cz.linkedlist.http

import cz.linkedlist.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.PeriodicTrigger
import org.springframework.test.util.ReflectionTestUtils

import java.time.LocalDate
import java.util.Arrays

import org.mockito.Mockito.verify

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@RunWith(MockitoJUnitRunner::class)
class HttpContinuousCheckingServiceTest {

    private var service: HttpContinuousCheckingService? = null

    @Mock
    protected var listingService: TileListingService? = null
    @Mock
    protected var infoService: TileInfoService? = null
    @Mock
    protected var scheduler: TaskScheduler? = null
    @Mock
    protected var downloader: TileDownloader? = null

    @Before
    fun init() {
        service = HttpContinuousCheckingService()
        MockitoAnnotations.initMocks(this)
        ReflectionTestUtils.setField(service, "listingService", listingService)
        ReflectionTestUtils.setField(service, "infoService", infoService)
        ReflectionTestUtils.setField(service, "scheduler", scheduler)
        ReflectionTestUtils.setField(service, "downloader", downloader)
    }

    @Test
    fun testCreateTask() {
        val task =
            DownloadTask(UTMCode.of("10SAA"), 100.0, LocalDate.now(), Arrays.asList<Contents>(TileSet.Contents.BAND_1))
        val trigger = PeriodicTrigger(1)
        service!!.createTask(task, trigger)

        verify<TaskScheduler>(scheduler).schedule(
            HttpDownloadTask(task, listingService, infoService, downloader),
            trigger
        )
    }

}
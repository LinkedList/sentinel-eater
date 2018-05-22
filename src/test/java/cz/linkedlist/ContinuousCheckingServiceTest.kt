package cz.linkedlist

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Spy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.support.CronTrigger
import org.springframework.test.context.junit4.SpringRunner

import java.time.LocalDate
import java.util.*

import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
@SpringBootTest
@RunWith(SpringRunner::class)
class ContinuousCheckingServiceTest {

    @Autowired
    @Spy
    private val testService: TestContinuousCheckingService? = null
    @Autowired
    private val jdbc: JdbcTemplate? = null

    @Before
    fun before() {
        jdbc!!.execute("TRUNCATE TABLE tasks")
    }

    @Test
    @Throws(Exception::class)
    fun register() {
        assertThat<TestContinuousCheckingService>(testService, not(nullValue()))

        testService!!.register(UTMCode.of("36MTD"), 65.0, arrayOf())
        val tasks = testService.list()

        assertThat(tasks, hasItem(DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), arrayOf())))
        Mockito.verify(testService).createTask(
            DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), arrayOf()),
            AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER
        )
    }

    @Test
    @Throws(Exception::class)
    fun registerWithTrigger() {
        assertThat<TestContinuousCheckingService>(testService, not(nullValue()))
        val t = CronTrigger("10 0 0 * * ?")
        testService!!.register(UTMCode.of("36MTD"), 65.0, arrayOf(), t)
        val tasks = testService.list()

        assertThat(tasks, hasItem(DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), arrayOf())))
        Mockito.verify(testService)
            .createTask(DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), arrayOf()), t)
    }

    @Test
    @Throws(Exception::class)
    fun registerWithContents() {
        assertThat<TestContinuousCheckingService>(testService, not(nullValue()))
        val contents = arrayOf(TileSet.Contents.BAND_1)
        testService!!.register(UTMCode.of("36MTD"), 65.0, contents)
        val tasks = testService.list()

        assertThat(tasks, hasItem(DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), contents)))
        Mockito.verify(testService).createTask(
            DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), contents),
            AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER
        )
    }

    @Test
    @Throws(Exception::class)
    fun registerWithMultipleContents() {
        assertThat<TestContinuousCheckingService>(testService, not(nullValue()))
        val contents = arrayOf(TileSet.Contents.BAND_1, TileSet.Contents.BAND_2)
        testService!!.register(UTMCode.of("36MTD"), 65.0, contents)
        val tasks = testService.list()

        assertThat(tasks, hasItem(DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), contents)))
        Mockito.verify(testService).createTask(
            DownloadTask(UTMCode.of("36MTD"), 65.0, LocalDate.now(), contents),
            AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER
        )
    }
}
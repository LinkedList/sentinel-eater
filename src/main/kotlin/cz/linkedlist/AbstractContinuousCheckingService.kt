package cz.linkedlist

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import org.springframework.util.StringUtils

import java.time.LocalDate

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
abstract class AbstractContinuousCheckingService : ContinuousCheckingService {

    protected val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val jdbc: JdbcTemplate? = null
    @Autowired
    protected var listingService: TileListingService? = null
    @Autowired
    protected var infoService: TileInfoService? = null
    @Autowired
    protected var scheduler: TaskScheduler? = null
    @Autowired
    protected var downloader: TileDownloader? = null

    override fun register(utm: UTMCode, cloudiness: Double?, contents: Array<TileSet.Contents>) {
        register(utm, cloudiness, contents, DEFAULT_CRON_TRIGGER)
    }

    override fun register(utm: UTMCode, cloudiness: Double?, contents: Array<TileSet.Contents>, trigger: Trigger) {
        log.info(
            "Registering a new task: utm: {}, cloudiness: {}, contents: {}, trigger: {}",
            utm,
            cloudiness,
            contents,
            trigger
        )
        //Nasty hack, nasty hack :D
        val contentsStr = StringBuilder()
        contents.forEach { c -> contentsStr.append(c).append(",") }

        jdbc!!.update(
            "insert into tasks VALUES (?, ?, ?, ?)",
            utm.toString(),
            LocalDate.now(),
            cloudiness,
            contentsStr.toString()
        )
        createTask(DownloadTask(utm, cloudiness, LocalDate.now(), contents), trigger)
    }

    override fun list(): Collection<DownloadTask> {
        return jdbc!!.query("select * from tasks") { resultSet, i ->
            val task = resultSet.let {
                DownloadTask(
                    cloudiness = it.getDouble("cloudiness"),
                    date = it.getDate("date").toLocalDate(),
                    utm = UTMCode.of(it.getString("utm")),
                    contents = if(StringUtils.hasLength(it.getString("contents")))
                                it.getString("contents").split(",".toRegex())
                                    .dropLastWhile { it.isEmpty() }
                                    .map { TileSet.Contents.valueOf(it) }.toTypedArray()
                                else emptyArray()
                )
            }
            task
        }
    }

    protected abstract fun createTask(task: DownloadTask, trigger: Trigger)

    companion object {
        /**
         * Default is daily at 01:00
         */
        val DEFAULT_CRON_TRIGGER = CronTrigger("0 0 1 * * ?")
    }
}

package cz.linkedlist.http


import cz.linkedlist.*
import org.slf4j.LoggerFactory
import org.springframework.util.concurrent.FailureCallback
import org.springframework.util.concurrent.SuccessCallback

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
class HttpDownloadTask(
    val task: DownloadTask? = null,
    val tileListingService: TileListingService? = null,
    val infoService: TileInfoService? = null,
    val downloader: TileDownloader? = null
) : Runnable {


    override fun run() {
        log.info("Checking for new tiles with UTM code: {}, after date: {}", task!!.utm, task.date)
        val dates = tileListingService!!.availableDatesAfter(task.utm!!, task.date!!)
        if (!dates.isEmpty()) {
            log.info("Found new tileSet for UTM code: {}, new tilesets: {}", task.utm, dates)
            val tiles = dates.map { localDate -> TileSet(task.utm, localDate) }
            val future = infoService!!.downTileInfo(tiles)
            future.addCallback(
                HttpDownloadTaskSuccess(task, downloader!!),
                FailureCallback { throwable -> log.error("There was an error while downloading info", throwable) }
                )
        } else {
            log.info("No new tiles found with UTM code: {}, after date: {}", task.utm, task.date)
        }
    }

    class HttpDownloadTaskSuccess(
        var task: DownloadTask? = null,
        val downloader: TileDownloader? = null,
        var desiredCloudinessTileSets: MutableList<TileSet> = mutableListOf()
    ) : SuccessCallback<List<TileSet>> {

        override fun onSuccess(tileSets: List<TileSet>) {
            val tileSetsWithDesiredCloudiness = tileSets
                .filter { it.cloudiness()!! < task!!.cloudiness!! }
            if (tileSetsWithDesiredCloudiness.isEmpty()) {
                log.info("No new tileSet with desired cloudiness")
            } else {
                log.info("Found new tileSets with desired cloudiness:")
                for (set in tileSetsWithDesiredCloudiness) {
                    log.info("Set: {}, cloudiness: {}", set, set.cloudiness())
                }

                desiredCloudinessTileSets!!.addAll(tileSetsWithDesiredCloudiness)
                if (downloader != null) {
                    tileSetsWithDesiredCloudiness.forEach { tileSet ->
                        downloader.downContent(
                            tileSet,
                            task!!.contents
                        )
                    }
                }
            }
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(HttpDownloadTask::class.java!!)
    }
}

package cz.linkedlist

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Service("down-info")
open class DownInfoServiceImpl : DownInfoService {

    companion object: KLogging()

    @Autowired
    @Lazy
    private val service: TileInfoService? = null

    @Async
    override fun downTileInfo(tileSet: TileSet): ListenableFuture<TileSet> {
        logger.debug("Downloading tileInfo for tileSet: {}", tileSet)
        val info = service!!.getTileInfo(tileSet)
        val set = tileSet.copy()
        set.info = (info)
        return AsyncResult(set)
    }
}

package cz.linkedlist.http

import cz.linkedlist.*
import cz.linkedlist.TileDownloader.Companion.DOWN_URL
import cz.linkedlist.info.ProductInfo
import cz.linkedlist.info.TileInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.client.RestTemplate


/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("http-info")
@Profile(SentinelEater.Profiles.HTTP)
@Transactional
open class HttpTileInfoService : TileInfoService {

    @Autowired
    private val listingService: TileListingService? = null
    @Autowired
    private val rest: RestTemplate? = null
    @Autowired
    private val downInfoService: DownInfoService? = null

    override fun getTileInfo(tileSet: TileSet): TileInfo {
        if (!listingService!!.exists(tileSet)) {
            throw RuntimeException("TileSet [$tileSet] doesn't exist. Sorry.")
        }

        return rest!!.getForObject(DOWN_URL + tileSet.tileInfo(), TileInfo::class.java)
    }

    override fun getProductInfo(tileSet: TileSet): ProductInfo {
        if (!listingService!!.exists(tileSet)) {
            throw RuntimeException("TileSet [$tileSet] doesn't exist. Sorry.")
        }

        return rest!!.getForObject(DOWN_URL + tileSet.productInfo(), ProductInfo::class.java)
    }

    override fun downTileInfo(tileSet: TileSet): ListenableFuture<TileSet> {
        return downInfoService!!.downTileInfo(tileSet)
    }

    @Async
    override fun downTileInfo(tileSets: Collection<TileSet>): ListenableFuture<List<TileSet>> {
        val futures = tileSets.map{
            downInfoService!!.downTileInfo(it)
        }
        return AsyncUtil.allOf(futures)
    }
}

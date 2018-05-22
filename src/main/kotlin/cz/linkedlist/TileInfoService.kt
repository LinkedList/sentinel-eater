package cz.linkedlist

import cz.linkedlist.info.ProductInfo
import cz.linkedlist.info.TileInfo
import org.springframework.util.concurrent.ListenableFuture

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
interface TileInfoService {
    fun getTileInfo(tileSet: TileSet): TileInfo

    fun getProductInfo(tileSet: TileSet): ProductInfo

    /**
     * Will download TileInfo for specified tileSet
     * @param tileSet
     * @return new instance of TileSet with downloaded TileInfo
     */
    fun downTileInfo(tileSet: TileSet): ListenableFuture<TileSet>

    fun downTileInfo(tileSets: Collection<TileSet>): ListenableFuture<List<TileSet>>
}

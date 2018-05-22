package cz.linkedlist

import org.springframework.util.concurrent.ListenableFuture

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
interface DownInfoService {

    /**
     * Will download TileInfo for specified tileSet
     * @param tileSet
     * @return new instance of TileSet with downloaded TileInfo
     */
    fun downTileInfo(tileSet: TileSet): ListenableFuture<TileSet>
}

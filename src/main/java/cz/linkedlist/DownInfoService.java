package cz.linkedlist;

import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface DownInfoService {

    /**
     * Will download TileInfo for specified tileSet
     * @param tileSet
     * @return new instance of TileSet with downloaded TileInfo
     */
    ListenableFuture<TileSet> downTileInfo(final TileSet tileSet);
}

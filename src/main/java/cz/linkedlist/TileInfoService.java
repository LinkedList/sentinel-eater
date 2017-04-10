package cz.linkedlist;

import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface TileInfoService {
    TileInfo getTileInfo(TileSet tileSet);

    ProductInfo getProductInfo(TileSet tileSet);

    /**
     * Will download TileInfo for specified tileSet
     * @param tileSet
     * @return new instance of TileSet with downloaded TileInfo
     */
    CompletableFuture<TileSet> downTileInfo(final TileSet tileSet);

    CompletableFuture<List<TileSet>> downTileInfo(final List<TileSet> tileSets);
}

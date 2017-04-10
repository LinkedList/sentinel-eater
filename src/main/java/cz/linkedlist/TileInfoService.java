package cz.linkedlist;

import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

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
    ListenableFuture<TileSet> downTileInfo(final TileSet tileSet);

    ListenableFuture<List<TileSet>> downTileInfo(final List<TileSet> tileSets);
}

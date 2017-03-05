package cz.linkedlist;

import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface TileInfoService {
    TileInfo getTileInfo(TileSet tileSet);

    ProductInfo getProductInfo(TileSet tileSet);
}

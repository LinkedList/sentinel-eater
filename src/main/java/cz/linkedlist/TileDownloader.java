package cz.linkedlist;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface TileDownloader {

    void downBand(TileSet tileSet, int band);

    void downBand8A(TileSet tileSet);

    void downProductInfo(TileSet tileSet);

    void downTileInfo(TileSet tileSet);

    void downMetadata(TileSet tileSet);

}

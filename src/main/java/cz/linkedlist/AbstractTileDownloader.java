package cz.linkedlist;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public abstract class AbstractTileDownloader implements TileDownloader {
    @Value(DESTINATION_FOLDER_PROP)
    protected String destinationFolder;


    @Override
    public void downBand(TileSet tileSet, int band) {
        tileSet.band(band)
                .map(s -> down(tileSet, s))
                .orElseThrow(() -> new RuntimeException("I am sorry, cannot download band: " + band));
    }

    @Override
    public void downBand8A(TileSet tileSet) {
        down(tileSet, tileSet.band8A());
    }

    @Override
    public void downProductInfo(TileSet tileSet) {
        down(tileSet, tileSet.productInfo());
    }

    @Override
    public void downTileInfo(TileSet tileSet) {
        down(tileSet, tileSet.tileInfo());
    }

    @Override
    public void downMetadata(TileSet tileSet) {
        down(tileSet, tileSet.metadata());
    }

    @Override
    public void downContent(TileSet tileSet, TileSet.Contents content) {
        this.down(tileSet, content.map(tileSet));
    }

    @Override
    public void downContent(TileSet tileSet, List<TileSet.Contents> contents) {
        contents.forEach(c -> this.downContent(tileSet, c));
    }

    protected abstract File down(TileSet tileSet, String what);

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }
}

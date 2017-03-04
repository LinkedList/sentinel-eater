package cz.linkedlist.http;

import cz.linkedlist.TileDownloader;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Service("http-downloader")
@Async
public class HttpTileDownloader implements TileDownloader {
    private static final String DOWN_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/";

    @Value(DESTINATION_FOLDER_PROP)
    private String destinationFolder;
    private final TileListingService listingService;

    public HttpTileDownloader(@Qualifier("http-listing") TileListingService tileListingService) {
        this.listingService = tileListingService;
    }

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

    private File down(TileSet tileSet, String what) {
        if(DESTINATION_FOLDER_PROP.equals(destinationFolder)) {
            throw new RuntimeException("destinationFolder property has to be set before downloading");
        }

        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("I cannot download something, that doesn't exist, sorry. TileSet: " + tileSet);
        }
        ensureFolderExists(destinationFolder);

        File file = new File(destinationFolder + what.replace("/", "_"));

        try {
            FileUtils.copyURLToFile(new URL(DOWN_URL + what), file);
        } catch (IOException O_o) {
            throw new RuntimeException("There was a problem downloading object from bucket", O_o);
        }
        return file;
    }

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }
}

package cz.linkedlist.http;

import cz.linkedlist.AbstractTileDownloader;
import cz.linkedlist.SentinelEater;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
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
@Profile(SentinelEater.Profiles.HTTP)
@RequiredArgsConstructor
public class HttpTileDownloader extends AbstractTileDownloader {
    protected final TileListingService listingService;

    protected File down(TileSet tileSet, String what) {
        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("I cannot download something, that doesn't exist, sorry. TileSet: " + tileSet);
        }
        ensureFolderExists(destinationFolder);
        File downloaded = isDownloaded(what);
        if(downloaded != null) {
            return downloaded;
        }

        File file = new File(destinationFolder + what.replace("/", "_"));

        try {
            FileUtils.copyURLToFile(new URL(DOWN_URL + what), file);
        } catch (IOException O_o) {
            throw new RuntimeException("There was a problem downloading object from bucket", O_o);
        }
        return file;
    }
}

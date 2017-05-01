package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import cz.linkedlist.AbstractTileDownloader;
import cz.linkedlist.SentinelEater;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static cz.linkedlist.SentinelEater.BUCKET;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("amazon-downloader")
@Async
@Slf4j
@Profile(SentinelEater.Profiles.AMAZON)
@RequiredArgsConstructor
public class AmazonSDKTileDownloader extends AbstractTileDownloader {

    private final AmazonS3Client client;
    protected final TileListingService listingService;

    protected File down(TileSet tileSet, String what) {
        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("I cannot download something, that doesn't exist, sorry. TileSet: " + tileSet);
        }

        ensureFolderExists(destinationFolder);
        File downloaded = isDownloaded(what);
        if(downloaded != null) {
            log.info("File [{}] already exists, don't have to download again.", downloaded);
            return downloaded;
        }

        GetObjectRequest request = new GetObjectRequest(BUCKET, what);
        File file = new File(destinationFolder + what.replace("/", "_"));
        try(
                S3Object s3Object = client.getObject(request);
                FileOutputStream fos = new FileOutputStream(file);
                S3ObjectInputStream objectInputStream = s3Object.getObjectContent()
        ) {
            IOUtils.copy(objectInputStream, fos);
            return file;
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem downloading object from bucket", ex);
        }
    }

}

package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import cz.linkedlist.TileDownloader;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static cz.linkedlist.SentinelEater.BUCKET;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service
@Async
public class AmazonSDKTileDownloader implements TileDownloader {

    @Value(DESTINATION_FOLDER_PROP)
    private String destinationFolder;
    private final AmazonS3Client client;
    private final TileListingService listingService;

    public AmazonSDKTileDownloader(AmazonS3Client client, @Qualifier("amazon") TileListingService listingService) {
        this.client = client;
        this.listingService = listingService;
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

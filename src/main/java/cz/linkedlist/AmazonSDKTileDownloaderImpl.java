package cz.linkedlist;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

import static cz.linkedlist.SentinelEater.BUCKET;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service
@RequiredArgsConstructor
public class AmazonSDKTileDownloaderImpl implements TileDownloader {

    private static final String TMP = "/tmp/sentinel/";
    private final AmazonS3Client client;

    @Override
    public void downBand(TileSet tileSet, int band) {
        tileSet.getBand(band).map(s -> {
            GetObjectRequest request = new GetObjectRequest(BUCKET, s);
            try(
                    S3Object s3Object = client.getObject(request);
                    FileOutputStream fos = new FileOutputStream(TMP + s.replace("/", "_"));
                    S3ObjectInputStream objectInputStream = s3Object.getObjectContent()
            ) {
                IOUtils.copy(objectInputStream, fos);
            } catch (IOException ex) {
                throw new RuntimeException("There was a problem downloading object from bucket", ex);
            }
            return null;
        }).orElseThrow(() -> new RuntimeException("I am sorry, cannot download band: " + band));
    }

    @Override
    public void downProductInfo(TileSet tileSet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void downTileInfo(TileSet tileSet) {
        throw new UnsupportedOperationException();
    }

}

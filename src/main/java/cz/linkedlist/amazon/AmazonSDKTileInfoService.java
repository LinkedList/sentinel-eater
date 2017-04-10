package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.linkedlist.SentinelEater;
import cz.linkedlist.TileInfoService;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cz.linkedlist.SentinelEater.BUCKET;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("amazon-info")
@Profile(SentinelEater.Profiles.AMAZON)
@RequiredArgsConstructor
public class AmazonSDKTileInfoService implements TileInfoService {

    private final TileListingService listingService;
    private final ObjectMapper mapper;
    private final AmazonS3Client client;

    @Override
    public TileInfo getTileInfo(TileSet tileSet) {
        return down(tileSet, TileInfo.class);
    }

    @Override
    public ProductInfo getProductInfo(TileSet tileSet) {
        return down(tileSet, ProductInfo.class);
    }

    @Override
    public CompletableFuture<TileSet> downTileInfo(final TileSet tileSet) {
        throw new UnsupportedOperationException("This feature is not yet implemented");
    }

    @Override
    public CompletableFuture<List<TileSet>> downTileInfo(List<TileSet> tileSets) {
        throw new UnsupportedOperationException("This feature is not yet implemented");
    }

    private <OBJ> OBJ down(TileSet tileSet, Class<OBJ> clazz) {
        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("TileSet [" + tileSet + "] doesn't exist. Sorry.");
        }

        GetObjectRequest request = new GetObjectRequest(BUCKET, tileSet.productInfo());
        try(
                S3Object s3Object = client.getObject(request);
                S3ObjectInputStream objectInputStream = s3Object.getObjectContent()
        ) {
            return mapper.readValue(objectInputStream, clazz);
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem downloading object from bucket", ex);
        }

    }
}

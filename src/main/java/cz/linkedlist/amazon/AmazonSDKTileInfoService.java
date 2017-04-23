package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.linkedlist.*;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.linkedlist.SentinelEater.BUCKET;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("amazon-info")
@Profile(SentinelEater.Profiles.AMAZON)
@Transactional
public class AmazonSDKTileInfoService implements TileInfoService {

    @Autowired
    private TileListingService listingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AmazonS3Client client;
    @Autowired
    private DownInfoService downInfoService;

    @Override
    public TileInfo getTileInfo(TileSet tileSet) {
        return down(tileSet, TileInfo.class);
    }

    @Override
    public ProductInfo getProductInfo(TileSet tileSet) {
        return down(tileSet, ProductInfo.class);
    }

    @Override
    public ListenableFuture<TileSet> downTileInfo(final TileSet tileSet) {
        return downInfoService.downTileInfo(tileSet);
    }

    @Override
    public ListenableFuture<List<TileSet>> downTileInfo(Collection<TileSet> tileSets) {
        final List<ListenableFuture<TileSet>> futures =
                tileSets.stream()
                        .map(downInfoService::downTileInfo)
                        .collect(Collectors.toList());
        return AsyncUtil.allOf(futures);
    }

    private <OBJ> OBJ down(TileSet tileSet, Class<OBJ> clazz) {
        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("TileSet [" + tileSet + "] doesn't exist. Sorry.");
        }

        GetObjectRequest request = new GetObjectRequest(BUCKET, key(tileSet, clazz));
        try(
                S3Object s3Object = client.getObject(request);
                S3ObjectInputStream objectInputStream = s3Object.getObjectContent()
        ) {
            return mapper.readValue(objectInputStream, clazz);
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem downloading object from bucket", ex);
        }
    }

    public static <OBJ> String key(TileSet tileSet, Class<OBJ> clazz) {
        if(TileInfo.class.equals(clazz)) {
            return tileSet.tileInfo();
        }
        if(ProductInfo.class.equals(clazz)) {
            return tileSet.productInfo();
        }

        throw new RuntimeException("This shouldn't happen. " +
                "Please check your arguments. clazz should be one of: TileInfo, ProductInfo. Instead supplied: " + clazz);
    }
}

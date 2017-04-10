package cz.linkedlist.http;

import cz.linkedlist.SentinelEater;
import cz.linkedlist.TileInfoService;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static cz.linkedlist.TileDownloader.DOWN_URL;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("http-info")
@Profile(SentinelEater.Profiles.HTTP)
@RequiredArgsConstructor
public class HttpTileInfoService implements TileInfoService {

    private final TileListingService listingService;
    private final RestTemplate rest;

    @Override
    public TileInfo getTileInfo(TileSet tileSet) {
        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("TileSet [" + tileSet + "] doesn't exist. Sorry.");
        }

        return rest.getForObject(DOWN_URL + tileSet.tileInfo(), TileInfo.class);
    }

    @Override
    public ProductInfo getProductInfo(TileSet tileSet) {
        if(!listingService.exists(tileSet)) {
            throw new RuntimeException("TileSet [" + tileSet + "] doesn't exist. Sorry.");
        }

        return rest.getForObject(DOWN_URL + tileSet.productInfo(), ProductInfo.class);
    }

    @Override
    @Async
    public ListenableFuture<TileSet> downTileInfo(final TileSet tileSet) {
        final TileInfo info = getTileInfo(tileSet);
        final TileSet set = new TileSet(tileSet);
        set.setInfo(info);
        return new AsyncResult<>(set);
    }

    @Override
    public ListenableFuture<List<TileSet>> downTileInfo(List<TileSet> tileSets) {
        final List<TileSet> list = new ArrayList<>();
        tileSets.forEach(tileSet -> {
            TileInfo info = getTileInfo(tileSet);
            final TileSet newSet = new TileSet(tileSet);
            newSet.setInfo(info);
            list.add(newSet);
        });

        return new AsyncResult<>(list);
    }
}

package cz.linkedlist.http;

import cz.linkedlist.*;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.linkedlist.TileDownloader.DOWN_URL;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("http-info")
@Profile(SentinelEater.Profiles.HTTP)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HttpTileInfoService implements TileInfoService {

    @Autowired
    private TileListingService listingService;
    @Autowired
    private RestTemplate rest;
    @Autowired
    private DownInfoService downInfoService;

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
    public ListenableFuture<TileSet> downTileInfo(final TileSet tileSet) {
        return downInfoService.downTileInfo(tileSet);
    }

    @Override
    @Async
    public ListenableFuture<List<TileSet>> downTileInfo(Collection<TileSet> tileSets) {
        final List<ListenableFuture<TileSet>> futures = tileSets.stream().map(downInfoService::downTileInfo).collect(Collectors.toList());
        return AsyncUtil.allOf(futures);
    }
}

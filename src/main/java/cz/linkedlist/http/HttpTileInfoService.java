package cz.linkedlist.http;

import cz.linkedlist.SentinelEater;
import cz.linkedlist.TileInfoService;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static cz.linkedlist.TileDownloader.DOWN_URL;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service
@Profile(SentinelEater.Profiles.HTTP)
public class HttpTileInfoService implements TileInfoService {

    private final TileListingService listingService;
    private final RestTemplate rest;

    public HttpTileInfoService(@Qualifier("http-listing") TileListingService listingService, RestTemplate restTemplate) {
        this.listingService = listingService;
        this.rest = restTemplate;
    }

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

}

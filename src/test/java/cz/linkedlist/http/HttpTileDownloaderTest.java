package cz.linkedlist.http;

import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.cache.Cache;
import org.mockito.Mockito;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileDownloaderTest extends AbstractTestNGSpringContextTests {

    private HttpTileListingService listingService;
    private HttpTileDownloader downloader;

    @BeforeTest
    public void init() {
        Cache cache = Mockito.mock(Cache.class);
        when(cache.exists(anyObject())).thenReturn(Optional.empty());
        when(cache.insert(anyObject(), eq(true))).thenReturn(true);
        when(cache.insert(anyObject(), eq(false))).thenReturn(false);
        listingService = new HttpTileListingService(new RestTemplate(), cache);
        downloader = new HttpTileDownloader(listingService);
        downloader.setDestinationFolder("/tmp/");
    }

    @Test
    public void testDownloadBand() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downProductInfo(tileSet);
        assertTrue(Files.exists(path));
        Files.deleteIfExists(path);
    }

}
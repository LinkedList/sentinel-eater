package cz.linkedlist.http;

import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.cache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileDownloaderTest {

    private HttpTileListingService listingService;
    private HttpTileDownloader downloader;

    @Before
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
    public void testDownloadProductInfo() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downProductInfo(tileSet);
        assertThat(Files.exists(path), is(true));
        assertTrue(Files.size(path) > 0);
        Files.deleteIfExists(path);
    }

    @Test
    public void testDownloadTileInfo() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.tileInfo().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downTileInfo(tileSet);
        assertThat(Files.exists(path), is(true));
        assertTrue(Files.size(path) > 0);
        Files.deleteIfExists(path);
    }

    @Test
    public void testMetadata() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.metadata().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downMetadata(tileSet);
        assertThat(Files.exists(path), is(true));
        assertTrue(Files.size(path) > 0);
        Files.deleteIfExists(path);
    }

    @Test
    public void testBand() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.band(1).get().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downBand(tileSet, 1);
        assertThat(Files.exists(path), is(true));
        assertTrue(Files.size(path) > 0);
        Files.deleteIfExists(path);
    }
}
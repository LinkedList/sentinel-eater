package cz.linkedlist.http;

import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.cache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileDownloaderTest {

    private HttpTileListingService listingService;
    private HttpTileDownloader downloader;

    @Before
    public void init() {
        Cache cache = mock(Cache.class);
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

    @Test
    public void testDownContent() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.band(1).get().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downContent(tileSet, TileSet.Contents.BAND_1);
        assertThat(Files.exists(path), is(true));
        assertTrue(Files.size(path) > 0);
        Files.deleteIfExists(path);
    }

    @Test
    public void testDownContents() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path1 = Paths.get("/tmp/" + tileSet.band(1).get().replace("/", "_"));
        Path path2 = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"));
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        downloader.downContent(tileSet, Arrays.asList(TileSet.Contents.BAND_1, TileSet.Contents.PROD_INFO));
        assertThat(Files.exists(path1), is(true));
        assertTrue(Files.size(path1) > 0);
        Files.deleteIfExists(path1);

        assertThat(Files.exists(path2), is(true));
        assertTrue(Files.size(path2) > 0);
        Files.deleteIfExists(path2);
    }

    @Test(expected = RuntimeException.class)
    public void testNotExists() {
        TileListingService listingService = mock(TileListingService.class);
        when(listingService.exists(any())).thenReturn(false);

        HttpTileDownloader downloader = new HttpTileDownloader(listingService);

        TileSet tileSet = new TileSet(UTMCode.of("36AAA"), LocalDate.of(2016, 8, 31));
        downloader.down(tileSet, tileSet.productInfo());
    }

    @Test
    public void testNotDownloaded() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"));
        Files.deleteIfExists(path);
        assertThat(downloader.isDownloaded(tileSet.productInfo().replace("/", "_")), is(nullValue()));
        downloader.downProductInfo(tileSet);
        assertThat(downloader.isDownloaded(tileSet.productInfo().replace("/", "_")), is(path.toFile()));
        Files.deleteIfExists(path);
    }

    @Test
    public void testIsDownloaded() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        Path path = Paths.get("/tmp/" + tileSet.productInfo().replace("/", "_"));
        Files.deleteIfExists(path);
        downloader.downProductInfo(tileSet);
        assertThat(downloader.isDownloaded(tileSet.productInfo().replace("/", "_")), is(path.toFile()));
        downloader.downProductInfo(tileSet);
        Files.deleteIfExists(path);
    }
}
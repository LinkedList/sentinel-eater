package cz.linkedlist.http;

import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.config.SpringTestNG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.testng.Assert.assertTrue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileDownloaderTest extends SpringTestNG {

    @Autowired
    private HttpTileDownloader downloader;

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
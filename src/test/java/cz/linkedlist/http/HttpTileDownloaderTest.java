package cz.linkedlist.http;

import cz.linkedlist.TileDownloader;
import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpTileDownloaderTest {

    @Autowired
    private TileDownloader downloader;

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
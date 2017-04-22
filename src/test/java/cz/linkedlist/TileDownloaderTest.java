package cz.linkedlist;

import cz.linkedlist.http.HttpTileDownloader;
import org.junit.After;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class TileDownloaderTest {

    private TileDownloader downloader = new HttpTileDownloader(mock(TileListingService.class));

    @After
    public void after() throws Exception {
        Path destFolder = Paths.get("/tmp/testFolder");
        if(Files.exists(destFolder)) {
            Files.delete(destFolder);
        }
    }

    @Test
    public void testEnsureFolderExistsReturns() throws Exception {
        String destFolder = "/tmp/testFolder";
        Files.createDirectory(Paths.get(destFolder));
        downloader.ensureFolderExists(destFolder);
        assertThat(Files.exists(Paths.get(destFolder)), is(true));
        assertThat(Files.isDirectory(Paths.get(destFolder)), is(true));
        Files.delete(Paths.get(destFolder));
    }

    @Test
    public void testEnsureFolderExistsCreates() throws Exception {
        String destFolder = "/tmp/testFolder";
        downloader.ensureFolderExists(destFolder);
        assertThat(Files.exists(Paths.get(destFolder)), is(true));
        assertThat(Files.isDirectory(Paths.get(destFolder)), is(true));
        Files.delete(Paths.get(destFolder));
    }

    @Test(expected = RuntimeException.class)
    public void testEnsureFolderNotExists() throws Exception {
        String destFolder = "/tmp/testFolder";
        Files.createFile(Paths.get(destFolder));
        downloader.ensureFolderExists(destFolder);
        Files.delete(Paths.get(destFolder));
    }
}
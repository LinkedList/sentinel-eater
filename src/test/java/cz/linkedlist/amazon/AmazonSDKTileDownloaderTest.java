package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@RunWith(MockitoJUnitRunner.class)
public class AmazonSDKTileDownloaderTest {

    @Mock
    private TileListingService listingService;
    @Mock
    private AmazonS3Client client;
    AmazonSDKTileDownloader downloader;

    @Before
    public void init() {
        downloader = new AmazonSDKTileDownloader(client, listingService);
    }

    @Test(expected = RuntimeException.class)
    public void testNotExists() {
        when(listingService.exists(any())).thenReturn(false);

        TileSet tileSet = new TileSet(UTMCode.of("36AAA"), LocalDate.of(2016, 8, 31));
        downloader.down(tileSet, tileSet.productInfo());
    }

    @Test
    public void testDownload() throws Exception {
        when(listingService.exists(any())).thenReturn(true);
        S3Object mockReturn  = mock(S3Object.class);
        when(client.getObject(any(GetObjectRequest.class))).thenReturn(mockReturn);
        when(mockReturn.getObjectContent()).thenReturn(new S3ObjectInputStream(IOUtils.toInputStream("test"), mock(HttpRequestBase.class)));
        downloader.setDestinationFolder("/tmp/testFolder/");
        TileSet tileSet = new TileSet(UTMCode.of("36AAA"), LocalDate.of(2016, 8, 31));
        downloader.down(tileSet, tileSet.productInfo());
        final Path downloadedFile = Paths.get("/tmp/testFolder/" + (tileSet.productInfo().replace("/", "_")));
        assertThat(Files.exists(downloadedFile));
        //tests already downloaded file
        downloader.down(tileSet, tileSet.productInfo());
        Files.delete(downloadedFile);
        Files.delete(Paths.get("/tmp/testFolder"));
    }

}
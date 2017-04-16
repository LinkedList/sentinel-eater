package cz.linkedlist.http;

import cz.linkedlist.*;
import cz.linkedlist.info.TileInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.SuccessCallback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HttpDownloadTaskTest {

    @Autowired
    private TileInfoService infoService;

    @Autowired
    private TileListingService listingService;

    @Test
    public void testRun() {
        DownloadTask downloadTask = new DownloadTask(UTMCode.of("36MTD"), 20D, LocalDate.of(2016, 5, 1));
        HttpDownloadTask task = new HttpDownloadTask(downloadTask, listingService, infoService);
        task.run();
    }

    @Test
    public void testSuccess() {
        TileSet set1 = TileSet.of("36MTD", 2016, 5, 1);
        TileSet set2 = TileSet.of("36MTD", 2016, 5, 2);
        TileInfo info1 = new TileInfo();
        info1.setCloudyPixelPercentage(50D);

        TileInfo info2 = new TileInfo();
        info2.setCloudyPixelPercentage(80D);

        set1.setInfo(info1);
        set2.setInfo(info2);

        DownloadTask task = new DownloadTask(UTMCode.of("36MTD"), 70D, LocalDate.of(2016, 5, 1));
        List<TileSet> successResultTileSets = new ArrayList<>();
        SuccessCallback<List<TileSet>> success = new HttpDownloadTask.HttpDownloadTaskSuccess(task, successResultTileSets);

        success.onSuccess(Arrays.asList(set1, set2));

        assertThat(successResultTileSets, hasSize(1));
        assertThat(successResultTileSets, hasItem(set1));
    }
}
package cz.linkedlist.http;

import cz.linkedlist.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.verify;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpContinuousCheckingServiceTest {

    private HttpContinuousCheckingService service;

    @Mock
    protected TileListingService listingService;
    @Mock
    protected TileInfoService infoService;
    @Mock
    protected TaskScheduler scheduler;
    @Mock
    protected TileDownloader downloader;

    @Before
    public void init() {
        service = new HttpContinuousCheckingService();
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "listingService", listingService);
        ReflectionTestUtils.setField(service, "infoService", infoService);
        ReflectionTestUtils.setField(service, "scheduler", scheduler);
        ReflectionTestUtils.setField(service, "downloader", downloader);
    }

    @Test
    public void testCreateTask() {
        DownloadTask task = new DownloadTask(UTMCode.of("10SAA"), 100D, LocalDate.now(), Arrays.asList(TileSet.Contents.BAND_1));
        Trigger trigger = new PeriodicTrigger(1);
        service.createTask(task, trigger);

        verify(scheduler).schedule(new HttpDownloadTask(task, listingService, infoService, downloader), trigger);
    }

}
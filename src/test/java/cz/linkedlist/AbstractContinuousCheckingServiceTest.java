package cz.linkedlist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AbstractContinuousCheckingServiceTest {

    @Autowired
    @Spy
    private TestContinuousCheckingService testService;
    @Autowired
    private JdbcTemplate jdbc;

    @Before
    public void before() {
        jdbc.execute("TRUNCATE TABLE tasks");
    }

    @Test
    public void register() throws Exception {
        assertThat(testService, not(nullValue()));

        testService.register(UTMCode.of("36MTD"), 65D, Collections.emptyList());
        Collection<DownloadTask> tasks = testService.list();

        assertThat(tasks, hasItem(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), Collections.emptyList())));
        Mockito.verify(testService).createTask(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), Collections.emptyList()), AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER);
    }

    @Test
    public void registerWithTrigger() throws Exception {
        assertThat(testService, not(nullValue()));
        CronTrigger t = new CronTrigger("10 0 0 * * ?");
        testService.register(UTMCode.of("36MTD"), 65D, Collections.emptyList(), t);
        Collection<DownloadTask> tasks = testService.list();

        assertThat(tasks, hasItem(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), Collections.emptyList())));
        Mockito.verify(testService).createTask(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), Collections.emptyList()), t);
    }

    @Test
    public void registerWithContents() throws Exception {
        assertThat(testService, not(nullValue()));
        List<TileSet.Contents> contents = Collections.singletonList(TileSet.Contents.BAND_1);
        testService.register(UTMCode.of("36MTD"), 65D, contents);
        Collection<DownloadTask> tasks = testService.list();

        assertThat(tasks, hasItem(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), contents)));
        Mockito.verify(testService).createTask(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), contents), AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER);
    }
    @Test
    public void registerWithMultipleContents() throws Exception {
        assertThat(testService, not(nullValue()));
        List<TileSet.Contents> contents = Arrays.asList(TileSet.Contents.BAND_1, TileSet.Contents.BAND_2);
        testService.register(UTMCode.of("36MTD"), 65D, contents);
        Collection<DownloadTask> tasks = testService.list();

        assertThat(tasks, hasItem(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), contents)));
        Mockito.verify(testService).createTask(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now(), contents), AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER);
    }
}
package cz.linkedlist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collection;

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

        testService.register(UTMCode.of("36MTD"), 65D);
        Collection<DownloadTask> tasks = testService.list();

        assertThat(tasks, hasItem(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now())));
        Mockito.verify(testService).createTask(new DownloadTask(UTMCode.of("36MTD"), 65D, LocalDate.now()), AbstractContinuousCheckingService.DEFAULT_CRON_TRIGGER);
    }

}
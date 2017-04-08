package cz.linkedlist;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class TestContinuousCheckingService extends AbstractContinuousCheckingService {

    public TestContinuousCheckingService(JdbcTemplate jdbc, TileListingService listingService, TileInfoService infoService, TaskScheduler scheduler) {
        super(jdbc, listingService, infoService, scheduler);
    }

    @Override
    protected void createTask(DownloadTask task) {
    }
}

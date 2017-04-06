package cz.linkedlist;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class TestContinuousCheckingService extends AbstractContinuousCheckingService {

    public TestContinuousCheckingService(JdbcTemplate jdbc, TileListingService tileListingService, TaskScheduler scheduler) {
        super(jdbc, tileListingService, scheduler);
    }

    @Override
    protected void createTask(DownloadTask task) {
    }
}

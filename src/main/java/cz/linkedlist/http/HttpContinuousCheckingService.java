package cz.linkedlist.http;

import cz.linkedlist.AbstractContinuousCheckingService;
import cz.linkedlist.DownloadTask;
import cz.linkedlist.SentinelEater;
import cz.linkedlist.TileListingService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Profile(SentinelEater.Profiles.HTTP)
@Service("http-continuous-checking")
public class HttpContinuousCheckingService extends AbstractContinuousCheckingService {

    public HttpContinuousCheckingService(
            JdbcTemplate jdbc,
            TileListingService tileListingService,
            TaskScheduler scheduler
    ) {
        super(jdbc, tileListingService, scheduler);
    }

    @Override
    protected void createTask(DownloadTask task) {
        scheduler.schedule(new HttpDownloadTask(task, tileListingService), new CronTrigger("10 * * * * ?"));
    }
}

package cz.linkedlist.http;

import cz.linkedlist.*;
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
            TileListingService listingService,
            TileInfoService infoService,
            TaskScheduler scheduler) {
        super(jdbc, listingService, infoService, scheduler);
    }

    @Override
    protected void createTask(DownloadTask task) {
        scheduler.schedule(new HttpDownloadTask(task, listingService, infoService), new CronTrigger("10 * * * * ?"));
    }
}

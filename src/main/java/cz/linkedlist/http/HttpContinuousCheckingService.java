package cz.linkedlist.http;

import cz.linkedlist.AbstractContinuousCheckingService;
import cz.linkedlist.DownloadTask;
import cz.linkedlist.SentinelEater;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Profile(SentinelEater.Profiles.HTTP)
@Service("http-continuous-checking")
public class HttpContinuousCheckingService extends AbstractContinuousCheckingService {

    @Override
    protected void createTask(DownloadTask task, CronTrigger trigger) {
        scheduler.schedule(new HttpDownloadTask(task, listingService, infoService, downloader), trigger);
    }
}

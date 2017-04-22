package cz.linkedlist.http;

import cz.linkedlist.AbstractContinuousCheckingService;
import cz.linkedlist.DownloadTask;
import cz.linkedlist.SentinelEater;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

@Profile(SentinelEater.Profiles.HTTP)
@Service("http-continuous-checking")
public class HttpContinuousCheckingService extends AbstractContinuousCheckingService {

    @Override
    protected void createTask(DownloadTask task, Trigger trigger) {
        log.info("Scheduling a new task: {}, with trigger: {}", task, trigger);
        scheduler.schedule(new HttpDownloadTask(task, listingService, infoService, downloader), trigger);
    }
}

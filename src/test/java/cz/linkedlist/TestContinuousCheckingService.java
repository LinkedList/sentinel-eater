package cz.linkedlist;

import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class TestContinuousCheckingService extends AbstractContinuousCheckingService {

    @Override
    protected void createTask(DownloadTask task, CronTrigger trigger) {
    }
}

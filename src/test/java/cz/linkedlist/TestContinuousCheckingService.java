package cz.linkedlist;

import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

@Service
public class TestContinuousCheckingService extends AbstractContinuousCheckingService {

    @Override
    protected void createTask(DownloadTask task, Trigger trigger) {
    }
}

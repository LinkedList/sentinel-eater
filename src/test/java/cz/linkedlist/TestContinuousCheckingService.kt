package cz.linkedlist

import org.springframework.scheduling.Trigger
import org.springframework.stereotype.Service

@Service
class TestContinuousCheckingService : AbstractContinuousCheckingService() {

    public override fun createTask(task: DownloadTask, trigger: Trigger) {}
}

package cz.linkedlist.http

import cz.linkedlist.AbstractContinuousCheckingService
import cz.linkedlist.DownloadTask
import cz.linkedlist.SentinelEater
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.Trigger
import org.springframework.stereotype.Service

@Profile(SentinelEater.Profiles.HTTP)
@Service("http-continuous-checking")
class HttpContinuousCheckingService : AbstractContinuousCheckingService() {

    public override fun createTask(task: DownloadTask, trigger: Trigger) {
        log.info("Scheduling a new task: {}, with trigger: {}", task, trigger)
        scheduler!!.schedule(HttpDownloadTask(task, listingService, infoService, downloader), trigger)
    }
}

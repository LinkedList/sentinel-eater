package cz.linkedlist.http;


import cz.linkedlist.DownloadTask;
import cz.linkedlist.TileListingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RequiredArgsConstructor
public class HttpDownloadTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(HttpDownloadTask.class);

    private final DownloadTask task;
    private final TileListingService tileListingService;

    @Override
    public void run() {
        log.info("Checking for new tiles with UTM code: {}, after date: {}", task.getUtm(), task.getDate());
        Collection<LocalDate> dates = tileListingService.availableDatesAfter(task.getUtm(), task.getDate());
        if(!dates.isEmpty()){
            log.info("Found new tileSet for UTM code: {}, new tilesets: {}", task.getUtm(), dates);
        } else {
            log.info("No new tiles found with UTM code: {}, after date: {}", task.getUtm(), task.getDate());
        }
    }
}

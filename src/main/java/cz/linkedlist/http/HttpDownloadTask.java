package cz.linkedlist.http;


import cz.linkedlist.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RequiredArgsConstructor
public class HttpDownloadTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(HttpDownloadTask.class);

    private final DownloadTask task;
    private final TileListingService tileListingService;
    private final TileInfoService infoService;
    private final TileDownloader downloader;

    @Override
    public void run() {
        log.info("Checking for new tiles with UTM code: {}, after date: {}", task.getUtm(), task.getDate());
        Collection<LocalDate> dates = tileListingService.availableDatesAfter(task.getUtm(), task.getDate());
        if(!dates.isEmpty()){
            log.info("Found new tileSet for UTM code: {}, new tilesets: {}", task.getUtm(), dates);
            Collection<TileSet> tiles = dates.stream()
                    .map(localDate -> new TileSet(task.getUtm(), localDate)).collect(Collectors.toList());
            final ListenableFuture<List<TileSet>> future = infoService.downTileInfo(tiles);
            future.addCallback(new HttpDownloadTaskSuccess(task, downloader), throwable -> log.error("There was an error while downloading info", throwable));
        } else {
            log.info("No new tiles found with UTM code: {}, after date: {}", task.getUtm(), task.getDate());
        }
    }

    public static class HttpDownloadTaskSuccess implements SuccessCallback<List<TileSet>> {

        private DownloadTask task;
        private List<TileSet> desiredCloudinessTileSets;
        private TileDownloader downloader;

        public HttpDownloadTaskSuccess(DownloadTask task, TileDownloader downloader) {
            this.task = task;
            this.downloader = downloader;
            this.desiredCloudinessTileSets = new ArrayList<>();
        }

        public HttpDownloadTaskSuccess(DownloadTask task, List<TileSet> desiredCloudinessTileSets) {
            this.task = task;
            this.desiredCloudinessTileSets = desiredCloudinessTileSets;
        }

        @Override
        public void onSuccess(List<TileSet> tileSets) {
            final List<TileSet> tileSetsWithDesiredCloudiness = tileSets.stream()
                    .filter(tileSet -> tileSet.cloudiness() < task.getCloudiness())
                    .collect(Collectors.toList());
            if (tileSetsWithDesiredCloudiness.isEmpty()) {
                log.info("No new tileSet with desired cloudiness");
            } else {
                log.info("Found new tileSets with desired cloudiness:");
                for (TileSet set : tileSetsWithDesiredCloudiness) {
                    log.info("Set: {}, cloudiness: {}", set, set.cloudiness());
                }

                desiredCloudinessTileSets.addAll(tileSetsWithDesiredCloudiness);
                if(downloader != null) {
                    tileSetsWithDesiredCloudiness.forEach(tileSet -> downloader.downContent(tileSet, task.getContents()));
                }
            }
        }
    }
}

package cz.linkedlist;

import cz.linkedlist.info.TileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("down-info")
@Slf4j
public class DownInfoServiceImpl implements DownInfoService {

    @Autowired
    @Lazy
    private TileInfoService service;

    @Async
    public ListenableFuture<TileSet> downTileInfo(final TileSet tileSet) {
        log.debug("Downloading tileInfo for tileSet: {}", tileSet);
        final TileInfo info = service.getTileInfo(tileSet);
        final TileSet set = new TileSet(tileSet);
        set.setInfo(info);
        return new AsyncResult<>(set);
    }
}

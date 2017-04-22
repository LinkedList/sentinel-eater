package cz.linkedlist;

import org.springframework.scheduling.Trigger;

import java.util.Collection;
import java.util.List;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public interface ContinuousCheckingService {

    /**
     * Method for registering new continuous checking task of desired tile.
     * @param utm utm of the tile
     * @param cloudiness desired minimal cloudiness in percents
     * @param contents
     */
    void register(final UTMCode utm, final Double cloudiness, List<TileSet.Contents> contents);

    /**
     * Method for registering new continuous checking task of desired tile.
     * @param utm utm of the tile
     * @param cloudiness desired minimal cloudiness in percents
     * @param contents
     * @param cron trigger expression
     */
    void register(final UTMCode utm, final Double cloudiness, List<TileSet.Contents> contents, Trigger cron);

    /**
     * Lists all currently registered tasks
     * @return list of all registered tasks
     */
    Collection<DownloadTask> list();

}

package cz.linkedlist;

import org.springframework.scheduling.support.CronTrigger;

import java.util.Collection;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public interface ContinuousCheckingService {

    /**
     * Method for registering new continuous checking task of desired tile.
     * @param utm utm of the tile
     * @param cloudiness desired minimal cloudiness in percents
     */
    void register(final UTMCode utm, final Double cloudiness);

    /**
     * Method for registering new continuous checking task of desired tile.
     * @param utm utm of the tile
     * @param cloudiness desired minimal cloudiness in percents
     * @param cron trigger expression
     */
    void register(final UTMCode utm, final Double cloudiness, CronTrigger cron);

    /**
     * Lists all currently registered tasks
     * @return list of all registered tasks
     */
    Collection<DownloadTask> list();

}

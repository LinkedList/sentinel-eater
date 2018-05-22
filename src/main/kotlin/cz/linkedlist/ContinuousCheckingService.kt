package cz.linkedlist

import org.springframework.scheduling.Trigger

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
interface ContinuousCheckingService {

    /**
     * Method for registering new continuous checking task of desired tile.
     * @param utm utm of the tile
     * @param cloudiness desired minimal cloudiness in percents
     * @param contents
     */
    fun register(utm: UTMCode, cloudiness: Double?, contents: Array<TileSet.Contents>)

    /**
     * Method for registering new continuous checking task of desired tile.
     * @param utm utm of the tile
     * @param cloudiness desired minimal cloudiness in percents
     * @param contents
     * @param cron trigger expression
     */
    fun register(utm: UTMCode, cloudiness: Double?, contents: Array<TileSet.Contents>, cron: Trigger)

    /**
     * Lists all currently registered tasks
     * @return list of all registered tasks
     */
    fun list(): Collection<DownloadTask>

}

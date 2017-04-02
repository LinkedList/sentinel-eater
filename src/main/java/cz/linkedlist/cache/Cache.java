package cz.linkedlist.cache;

import cz.linkedlist.TileSet;

import java.util.Optional;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface Cache {
    /**
     * Checks cache for record.
     * @param tileSet tileSet to find in cache
     * @return Optional boolean
     */
    Optional<Boolean> exists(TileSet tileSet);

    /**
     * Adds new record to cache.
     * @param tileSet tileSet for caching
     * @param exists if the tileset exists or not
     * @return exists param
     */
    boolean insert(TileSet tileSet, boolean exists);
}

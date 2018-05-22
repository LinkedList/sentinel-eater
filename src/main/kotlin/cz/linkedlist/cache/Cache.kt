package cz.linkedlist.cache

import cz.linkedlist.TileSet

import java.util.Optional

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
interface Cache {
    /**
     * Checks cache for record.
     * @param tileSet tileSet to find in cache
     * @return Optional boolean
     */
    fun exists(tileSet: TileSet): Optional<Boolean>

    /**
     * Adds new record to cache.
     * @param tileSet tileSet for caching
     * @param exists if the tileset exists or not
     * @return exists param
     */
    fun insert(tileSet: TileSet, exists: Boolean): Boolean
}

package cz.linkedlist.cache;

import cz.linkedlist.TileSet;

import java.util.Optional;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface Cache {
    Optional<Boolean> exists(TileSet tileSet);

    boolean insert(TileSet tileSet, boolean exists);
}

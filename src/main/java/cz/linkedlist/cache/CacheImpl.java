package cz.linkedlist.cache;

import cz.linkedlist.TileSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CacheImpl implements Cache {

    private static final Logger log = LoggerFactory.getLogger(CacheImpl.class);
    private final JdbcTemplate jdbc;

    @Override
    public Optional<Boolean> exists(TileSet tileSet) {
        log.debug("Checking cache for tileSet: {}", tileSet);
        final List<Boolean> exists = jdbc.queryForList(
                "select \"exists\" from cache where utm = ? and \"date\" = ?",
                new Object[]{
                        tileSet.getCode().toString(),
                        tileSet.getDate()
                },
                Boolean.class
        );
        if(exists.isEmpty()) {
            log.debug("Cache miss for tileSet: {}", tileSet);
            return Optional.empty();
        } else {
            boolean result = exists.get(0);
            log.debug("Cache hit for tileSet: {}, status: {}", tileSet, result);
            return Optional.of(result);
        }
    }

    @Override
    public boolean insert(TileSet tileSet, boolean exists) {
        log.debug("Inserting to cache tileSet: {}, exists: {}", tileSet, exists);
        jdbc.update(
                "insert into cache VALUES (?, ?, ?)",
                tileSet.getCode().toString(),
                tileSet.getDate(),
                exists
        );
        return exists;
    }
}

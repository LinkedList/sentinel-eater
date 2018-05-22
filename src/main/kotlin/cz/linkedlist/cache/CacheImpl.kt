package cz.linkedlist.cache

import cz.linkedlist.TileSet
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Service
@Transactional
open class CacheImpl : Cache {
    private val jdbc: JdbcTemplate? = null

    override fun exists(tileSet: TileSet): Optional<Boolean> {
        log.debug("Checking cache for tileSet: {}", tileSet)
        val exists = jdbc!!.queryForList<Boolean>(
            "select \"exists\" from cache where utm = ? and \"date\" = ?",
            arrayOf(tileSet.code.toString(), tileSet.date),
            Boolean::class.java
        )
        return if (exists.isEmpty()) {
            log.debug("Cache miss for tileSet: {}", tileSet)
            Optional.empty()
        } else {
            val result = exists[0]
            log.debug("Cache hit for tileSet: {}, status: {}", tileSet, result)
            Optional.of(result)
        }
    }

    override fun insert(tileSet: TileSet, exists: Boolean): Boolean {
        log.debug("Inserting to cache tileSet: {}, exists: {}", tileSet, exists)
        jdbc!!.update(
            "merge into cache VALUES (?, ?, ?)",
            tileSet.code.toString(),
            tileSet.date,
            exists
        )
        return exists
    }

    companion object {

        private val log = LoggerFactory.getLogger(CacheImpl::class.java!!)
    }
}

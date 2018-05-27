package cz.linkedlist.cache

import cz.linkedlist.TileSet
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit4.SpringRunner

import java.util.Optional

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@SpringBootTest
@RunWith(SpringRunner::class)
class CacheTest {

    @Autowired
    private val cache: Cache? = null

    @Autowired
    private val jdbc: JdbcTemplate? = null

    @Before
    fun before() {
        jdbc!!.execute("TRUNCATE TABLE cache")
    }

    @Test
    @Throws(Exception::class)
    fun testExistsNoInsert() {
        val tileSet = TileSet.of("36MTD", 2016, 8, 31)
        assertThat(cache!!.exists(tileSet), `is`<Optional<out Any>>(Optional.empty()))
    }

    @Test
    @Throws(Exception::class)
    fun testExistsInsert() {
        val tileSet = TileSet.of("36MTD", 2016, 8, 31)
        cache!!.insert(tileSet, true)
        assertThat(cache.exists(tileSet), `is`(Optional.of(java.lang.Boolean.TRUE)))
    }

    @Test
    @Throws(Exception::class)
    fun testNotExistsInsert() {
        val tileSet = TileSet.of("36MTD", 2016, 8, 31)
        cache!!.insert(tileSet, false)
        assertThat(cache.exists(tileSet), `is`(Optional.of(java.lang.Boolean.FALSE)))
    }
}
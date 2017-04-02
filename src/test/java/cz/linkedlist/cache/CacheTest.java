package cz.linkedlist.cache;

import cz.linkedlist.TileSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CacheTest {

    @Autowired
    private Cache cache;

    @Autowired
    private JdbcTemplate jdbc;

    @Before
    public void before() {
        jdbc.execute("TRUNCATE TABLE cache");
    }

    @Test
    public void testExistsNoInsert() throws Exception {
        TileSet tileSet = TileSet.of("36MTD",2016, 8, 31);
        assertThat(cache.exists(tileSet), is(Optional.empty()));
    }

    @Test
    public void testExistsInsert() throws Exception {
        TileSet tileSet = TileSet.of("36MTD",2016, 8, 31);
        cache.insert(tileSet, true);
        assertThat(cache.exists(tileSet), is(Optional.of(Boolean.TRUE)));
    }

    @Test
    public void testNotExistsInsert() throws Exception {
        TileSet tileSet = TileSet.of("36MTD",2016, 8, 31);
        cache.insert(tileSet, false);
        assertThat(cache.exists(tileSet), is(Optional.of(Boolean.FALSE)));
    }
}
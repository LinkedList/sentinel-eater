package cz.linkedlist;

import cz.linkedlist.http.HttpTileListingService;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileListingServiceTest extends AbstractTestNGSpringContextTests {

    private HttpTileListingService service;

    @BeforeTest
    public void init() {
        service = new HttpTileListingService();
    }

    @Test
    public void testExists() throws Exception {
        TileSet tileSet = new TileSet(new UTMCode(36,"M", "TD"), LocalDate.of(2016, 8, 31), 0);
        assertThat(service.exists(tileSet), is(true));
    }
}
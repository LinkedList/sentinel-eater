package cz.linkedlist;

import cz.linkedlist.http.HttpTileListingService;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31), 0);
        assertThat(service.exists(tileSet), is(true));
    }

    @Test
    public void testNotExists() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31), 3);
        assertThat(service.exists(tileSet), is(false));
    }

    @Test
    public void testGetYears() throws Exception {
        UTMCode code = UTMCode.of("36MTD");
        Set<Integer> years = service.getYears(code);
        assertThat(years, hasSize(3));
        assertThat(years, hasItem(2015));
        assertThat(years, hasItem(2016));
        assertThat(years, hasItem(2017));
    }

    @Test
    public void testGetMonths() throws Exception {
        UTMCode code = UTMCode.of("36MTD");
        Set<Integer> months = service.getMonths(code, 2015);
        assertThat(months, hasSize(5));
        assertThat(months, hasItem(10));
        assertThat(months, hasItem(11));
        assertThat(months, hasItem(12));
        assertThat(months, hasItem(7));
        assertThat(months, hasItem(9));
    }

    @Test
    public void testGetDays() throws Exception {
        UTMCode code = UTMCode.of("36MTD");
        Set<Integer> days = service.getDays(code, 2015, 10);
        assertThat(days, hasSize(2));
        assertThat(days, hasItem(23));
        assertThat(days, hasItem(3));
    }

    @Test
    public void testGetDatasets() throws Exception {
        UTMCode code = UTMCode.of("36MTD");
        Set<Integer> dataSets = service.getDataSets(code, 2015, 10, 23);
        assertThat(dataSets, hasSize(1));
        assertThat(dataSets, hasItem(0));
    }
}

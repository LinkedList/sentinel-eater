package cz.linkedlist.http;

import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.cache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileListingServiceTest {

    private HttpTileListingService service;

    @Before
    public void init() {
        Cache cache = Mockito.mock(Cache.class);
        when(cache.exists(anyObject())).thenReturn(Optional.empty());
        when(cache.insert(anyObject(), eq(true))).thenReturn(true);
        when(cache.insert(anyObject(), eq(false))).thenReturn(false);
        service = new HttpTileListingService(new RestTemplate(), cache);
    }

    @Test
    public void testExists() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
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

    @Test
    public void testGetFolderContents() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2015, 10, 23));
        List<String> folderContents = service.getFolderContents(tileSet);

        assertAllBands(folderContents);
        assertThat(folderContents, hasItem("tiles/36/M/TD/2015/10/23/0/metadata.xml"));
        assertThat(folderContents, hasItem("tiles/36/M/TD/2015/10/23/0/productInfo.json"));
        assertThat(folderContents, hasItem("tiles/36/M/TD/2015/10/23/0/tileInfo.json"));
    }

    @Test
    public void testAvailableDates() throws Exception {
        UTMCode code = UTMCode.of("20MMN");
        List<LocalDate> localDates = service.availableDates(code);
        assertThat(localDates, hasSize(1));
        assertThat(localDates, hasItem(LocalDate.of(2015, 12, 6)));
    }

    @Test
    public void testAvailableDatesAfter() throws Exception {
        UTMCode code = UTMCode.of("20MMN");
        Collection<LocalDate> localDates = service.availableDatesAfter(code, LocalDate.of(2014, 1, 6));
        assertThat(localDates, hasSize(1));
        assertThat(localDates, hasItem(LocalDate.of(2015, 12, 6)));
    }

    @Test
    public void testAvailableDatesAfterWithoutAvailableDates() throws Exception {
        UTMCode code = UTMCode.of("20MMN");
        Collection<LocalDate> localDates = service.availableDatesAfter(code, LocalDate.of(2017, 1, 6));
        assertThat(localDates, hasSize(0));
    }

    @Test
    public void testAvailableDatesAfterWithDateInTheSameYearAndMonth() throws Exception {
        UTMCode code = UTMCode.of("20MMN");
        Collection<LocalDate> localDates = service.availableDatesAfter(code, LocalDate.of(2015, 12, 5));
        assertThat(localDates, hasSize(1));
        assertThat(localDates, hasItem(LocalDate.of(2015, 12, 6)));
    }

    @Test
    public void testAvailableDatesAfterAreSortedAscendingly() throws Exception {
        Collection<LocalDate> localDates = service.availableDatesAfter(UTMCode.of("36MTD"), LocalDate.of(2017, 1, 1));
        LocalDate previous = null;
        for (LocalDate localDate : localDates) {
            if(previous == null) {
                previous = localDate;
                continue;
            }
            assertThat(previous, lessThan(localDate));
            previous = localDate;
        }
    }

    private void assertAllBands(List<String> contents) {
        for(int i = 1; i<=12; i++) {
            assertThat(contents, hasItem("tiles/36/M/TD/2015/10/23/0/B" + String.format("%02d", i) + ".jp2"));
        }
        assertThat(contents, hasItem("tiles/36/M/TD/2015/10/23/0/B8A.jp2"));
    }
}

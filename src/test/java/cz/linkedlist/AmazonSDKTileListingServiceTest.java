package cz.linkedlist;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class AmazonSDKTileListingServiceTest {

    private TileListingService service;

    @Test
    public void testGetYears() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        UTMCode code = new UTMCode(10, "M", "AB");
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.setCommonPrefixes(Arrays.asList(
                SentinelEater.TILES + code.toString() + "2015/",
                SentinelEater.TILES + code.toString() + "2016/",
                SentinelEater.TILES + code.toString() + "2017/"
        ));
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);

        service = new AmazonSDKTileListingService(client);

        Set<Integer> years = service.getYears(code);

        assertThat(years, hasSize(3));
        assertThat(years, hasItem(2015));
        assertThat(years, hasItem(2016));
        assertThat(years, hasItem(2017));
    }

    @Test
    public void testGetMonths() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        UTMCode code = new UTMCode(10, "M", "AB");
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.setCommonPrefixes(Arrays.asList(
                SentinelEater.TILES + code.toString() + "2015/1/",
                SentinelEater.TILES + code.toString() + "2015/2/",
                SentinelEater.TILES + code.toString() + "2015/3/"
        ));
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);

        service = new AmazonSDKTileListingService(client);

        Set<Integer> months = service.getMonths(code, 2015);

        assertThat(months, hasSize(3));
        assertThat(months, hasItem(1));
        assertThat(months, hasItem(2));
        assertThat(months, hasItem(3));
    }

    @Test
    public void testGetDays() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        UTMCode code = new UTMCode(10, "M", "AB");
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.setCommonPrefixes(Arrays.asList(
                SentinelEater.TILES + code.toString() + "2015/1/15/",
                SentinelEater.TILES + code.toString() + "2015/1/18/",
                SentinelEater.TILES + code.toString() + "2015/1/25/"
        ));
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);

        service = new AmazonSDKTileListingService(client);

        Set<Integer> days = service.getDays(code, 2015, 1);

        assertThat(days, hasSize(3));
        assertThat(days, hasItem(15));
        assertThat(days, hasItem(18));
        assertThat(days, hasItem(25));
    }

    @Test
    public void testGetDataSets() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        UTMCode code = new UTMCode(10, "M", "AB");
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.setCommonPrefixes(Arrays.asList(
                SentinelEater.TILES + code.toString() + "2015/1/15/0/",
                SentinelEater.TILES + code.toString() + "2015/1/15/1/"
        ));
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);

        service = new AmazonSDKTileListingService(client);

        Set<Integer> dataSets = service.getDataSets(code, 2015, 1, 15);

        assertThat(dataSets, hasSize(2));
        assertThat(dataSets, hasItem(0));
        assertThat(dataSets, hasItem(1));

    }

    @Test
    public void testExists() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        TileSet tileSet = new TileSet(new UTMCode(36,"M", "TD"), LocalDate.of(2016, 8, 31), 1);
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.setKeyCount(1);
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);
        service = new AmazonSDKTileListingService(client);

        assertThat(service.exists(tileSet), is(true));
    }

    @Test
    public void testExistsNotExists() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        TileSet tileSet = new TileSet(new UTMCode(36,"M", "TD"), LocalDate.of(2016, 8, 31), 1);
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.setKeyCount(0);
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);
        service = new AmazonSDKTileListingService(client);

        assertThat(service.exists(tileSet), is(false));
    }

    @Test
    public void testAvailableDatesUtmCode() throws Exception {
        AmazonS3Client client = mock(AmazonS3Client.class);
        UTMCode code = new UTMCode(33,"U", "XQ");
        ListObjectsV2Result list = new ListObjectsV2Result();
        list.getObjectSummaries().add(summary("tiles/33/U/XQ/2016/8/31/0/B01.jp2"));
        list.getObjectSummaries().add(summary("tiles/33/U/XQ/2017/8/30/0/B01.jp2"));
        list.getObjectSummaries().add(summary("tiles/33/U/XQ/2017/2/14/0/B01.jp2"));
        when(client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(list);
        service = new AmazonSDKTileListingService(client);

        List<LocalDate> dates = service.availableDates(code);
        assertThat(dates, hasSize(3));
        assertThat(dates, hasItem(LocalDate.of(2016, 8, 31)));
        assertThat(dates, hasItem(LocalDate.of(2017, 8, 30)));
        assertThat(dates, hasItem(LocalDate.of(2017, 2, 14)));
    }

    private S3ObjectSummary summary(String key) {
        S3ObjectSummary summary = new S3ObjectSummary();
        summary.setKey(key);
        return summary;
    }
}

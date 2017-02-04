package cz.linkedlist;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Macko <martin.macko@clevermaps.cz>.
 */
public class TileListingServiceTest {

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

        service = new TileListingService(client);

        Set<Integer> years = service.getYears(code);

        assertThat(years, hasSize(3));
        assertThat(years, hasItem(2015));
        assertThat(years, hasItem(2016));
        assertThat(years, hasItem(2017));
    }

}

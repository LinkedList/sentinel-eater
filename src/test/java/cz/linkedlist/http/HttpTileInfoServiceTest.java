package cz.linkedlist.http;

import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class HttpTileInfoServiceTest {

    @Autowired
    private HttpTileInfoService service;
    @Autowired
    private JdbcTemplate jdbc;

    @Before
    public void init() {
        jdbc.execute("TRUNCATE TABLE cache");
    }

    @Test
    public void downloadTileInfo() {
        final TileInfo tileInfo = service.getTileInfo(new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31)));
        assertThat(tileInfo.getUtmZone(), is(36));
        assertThat(tileInfo.getLatitudeBand(), is("M"));
        assertThat(tileInfo.getGridSquare(), is("TD"));
        assertThat(tileInfo.getCloudyPixelPercentage(), is(0.06D));
        assertThat(tileInfo.getPath(), is("tiles/36/M/TD/2016/8/31/0"));
    }

    @Test
    public void downloadProductInfo() {
        final ProductInfo productInfo = service.getProductInfo(new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31)));
        assertThat(productInfo.getName(), is("S2A_OPER_PRD_MSIL1C_PDMC_20160831T150507_R078_V20160831T080612_20160831T082520"));
    }

    @Test
    public void testDownloadTileInfoAndSetToTileSet() {
        final TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        final ListenableFuture<TileSet> f = service.downTileInfo(tileSet);
        f.addCallback(
                set -> {
                    assertThat(set, not(tileSet));
                    assertThat(tileSet.getInfo(), not(nullValue()));
                    assertThat(tileSet.cloudiness(), is(0.06D));
                },
                throwable -> Assert.fail());
    }
}

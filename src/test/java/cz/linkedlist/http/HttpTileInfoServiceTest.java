package cz.linkedlist.http;

import cz.linkedlist.TileInfoService;
import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.info.TileInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpTileInfoServiceTest {

    @Autowired
    private TileInfoService service;

    @Test
    public void testGet() throws Exception {
        TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        TileInfo info = service.get(tileSet);
        assertThat(info, notNullValue());
        assertThat(info.getPath(), is("tiles/36/M/TD/2016/8/31/0"));
        assertThat(info.getUtmZone(), is(36));
        assertThat(info.getLatitudeBand(), is("M"));
        assertThat(info.getGridSquare(), is("TD"));
    }

}
package cz.linkedlist.http;

import cz.linkedlist.TileInfoService;
import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.config.SpringTestNG;
import cz.linkedlist.info.TileInfo;
import org.hamcrest.CoreMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class HttpTileInfoServiceTest extends SpringTestNG {

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
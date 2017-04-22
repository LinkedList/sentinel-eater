package cz.linkedlist;

import cz.linkedlist.info.TileInfo;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static cz.linkedlist.TileSet.Contents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class TileSetTest {

    @Test
    public void testToString() throws Exception {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 10);
        Integer setOrder = 0;

        TileSet tileSet = new TileSet(code, date, setOrder);

        assertThat(tileSet.toString(), is("tiles/28/C/DD/2017/3/10/0/"));
    }

    @Test
    public void testDefaultBand() throws Exception {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 10);

        TileSet tileSet = new TileSet(code, date);

        assertThat(tileSet.toString(), is("tiles/28/C/DD/2017/3/10/0/"));
    }
    @Test
    public void testToStringsingleDay() throws Exception {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;

        TileSet tileSet = new TileSet(code, date, setOrder);

        assertThat(tileSet.toString(), is("tiles/28/C/DD/2017/3/9/0/"));
    }

    @Test
    public void getBandTest() {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;
        TileSet tileSet = new TileSet(code, date, setOrder);

        assertThat(tileSet.band(0), is(Optional.empty()));
        assertThat(tileSet.band(13), is(Optional.empty()));
        assertThat(tileSet.band(2).get(), is(tileSet.toString() + "B02.jp2"));
        assertThat(tileSet.band(11).get(), is(tileSet.toString() + "B11.jp2"));
        assertThat(tileSet.band8A(), is(tileSet.toString() + "B8A.jp2"));
    }

    @Test
    public void getOtherFilesTest() {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;
        TileSet tileSet = new TileSet(code, date, setOrder);

        assertThat(tileSet.productInfo(), is(tileSet.toString() + "productInfo.json"));
        assertThat(tileSet.metadata(), is(tileSet.toString() + "metadata.xml"));
        assertThat(tileSet.tileInfo(), is(tileSet.toString() + "tileInfo.json"));
    }

    @Test
    public void getNewInstance() {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;
        TileSet tileSet = new TileSet(code, date, setOrder);
        TileSet clone = new TileSet(tileSet);

        assertThat(tileSet, not(sameInstance(clone)));
        assertThat(tileSet, is(clone));
    }

    @Test(expected = RuntimeException.class)
    public void getCloudinessWithout() {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;
        TileSet tileSet = new TileSet(code, date, setOrder);
        tileSet.cloudiness();
    }

    @Test
    public void getCloudinessWith() {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;
        TileSet tileSet = new TileSet(code, date, setOrder);
        TileInfo info = new TileInfo();
        info.setCloudyPixelPercentage(50D);
        tileSet.setInfo(info);

        assertThat(tileSet.cloudiness(), is(50D));
    }

    @Test
    public void testContentsMap() {
        TileSet tileSet = TileSet.of("28CDD", 2017, 3, 10);

        assertThat(BAND_1.map(tileSet), is(tileSet.band(1).get()));
        assertThat(BAND_2.map(tileSet), is(tileSet.band(2).get()));
        assertThat(BAND_3.map(tileSet), is(tileSet.band(3).get()));
        assertThat(BAND_4.map(tileSet), is(tileSet.band(4).get()));
        assertThat(BAND_5.map(tileSet), is(tileSet.band(5).get()));
        assertThat(BAND_6.map(tileSet), is(tileSet.band(6).get()));
        assertThat(BAND_7.map(tileSet), is(tileSet.band(7).get()));
        assertThat(BAND_8.map(tileSet), is(tileSet.band(8).get()));
        assertThat(BAND_9.map(tileSet), is(tileSet.band(9).get()));
        assertThat(BAND_10.map(tileSet), is(tileSet.band(10).get()));
        assertThat(BAND_11.map(tileSet), is(tileSet.band(11).get()));
        assertThat(BAND_12.map(tileSet), is(tileSet.band(12).get()));
        assertThat(BAND_8A.map(tileSet), is(tileSet.band8A()));
        assertThat(PROD_INFO.map(tileSet), is(tileSet.productInfo()));
        assertThat(TILE_INFO.map(tileSet), is(tileSet.tileInfo()));
        assertThat(METADATA.map(tileSet), is(tileSet.metadata()));
    }

}
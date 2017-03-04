package cz.linkedlist;

import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
}
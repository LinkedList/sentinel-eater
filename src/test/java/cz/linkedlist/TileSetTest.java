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

        assertThat(tileSet.getBand(0), is(Optional.empty()));
        assertThat(tileSet.getBand(13), is(Optional.empty()));
        assertThat(tileSet.getBand(2).get(), is(tileSet.toString() + "B02.jp2"));
        assertThat(tileSet.getBand(11).get(), is(tileSet.toString() + "B11.jp2"));
    }
}
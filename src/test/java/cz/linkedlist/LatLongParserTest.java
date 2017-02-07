package cz.linkedlist;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class LatLongParserTest {

    @Test
    public void testParse() throws Exception {
        UTMCode code = LatLongParser.parse(10.5, 11.3);

        assertThat(code.getGridZoneDesignator(), is(36));
        assertThat(code.getLatitudeBand(), is("M"));
        assertThat(code.getSquare(), is("TD"));
    }

}
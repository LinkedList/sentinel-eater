package cz.linkedlist;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class UTMCodeTest {
    @Test
    public void testToString() throws Exception {
        UTMCode code = new UTMCode(36, "M", "TD");
        assertThat(code.toString(), is("36/M/TD/"));
    }

    @Test
    public void testOf() throws Exception {
        UTMCode code = UTMCode.of("36MTD");
        assertThat(code.toString(), is("36/M/TD/"));
    }

    @Test
    public void testOfWithSlashes() throws Exception {
        UTMCode code = UTMCode.of("36/M/TD/");
        assertThat(code.toString(), is("36/M/TD/"));
    }

    @Test
    public void testOfWithLeadingZero() throws Exception {
        UTMCode code = UTMCode.of("06MTD");
        assertThat(code.toString(), is("6/M/TD/"));
    }

}
package cz.linkedlist;

import org.testng.annotations.Test;

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

}
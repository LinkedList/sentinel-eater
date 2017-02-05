package cz.linkedlist;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class DataSetTest {

    @Test
    public void testToString() throws Exception {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 10);
        Integer setOrder = 0;

        DataSet dataSet = new DataSet(code, date, setOrder);

        assertThat(dataSet.toString(), is("tiles/28/C/DD/2017/3/10/0/"));
    }

    @Test
    public void testToStringsingleDay() throws Exception {
        UTMCode code = new UTMCode(28, "C", "DD");
        LocalDate date = LocalDate.of(2017, 3, 9);
        Integer setOrder = 0;

        DataSet dataSet = new DataSet(code, date, setOrder);

        assertThat(dataSet.toString(), is("tiles/28/C/DD/2017/3/9/0/"));
    }
}
package cz.linkedlist;


import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

/**
 * @author Martin Macko <martin.macko@clevermaps.cz>.
 */
public class DateParserTest {

    @Test
    public void testParse() throws Exception {
        S3ObjectSummary obj = new S3ObjectSummary();
        obj.setKey("tiles/36/M/TD/2017/1/15/0/B01.jp2");
        List<S3ObjectSummary> objects = new ArrayList<>();
        objects.add(obj);

        List<LocalDate> dates = DateParser.parse(objects);
        assertThat(dates, hasItem(LocalDate.of(2017, 1, 15)));
    }
}
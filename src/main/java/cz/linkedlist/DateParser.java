package cz.linkedlist;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Macko <martin.macko@clevermaps.cz>.
 */
public class DateParser {

    private static final Pattern DATE_PATTERN = Pattern.compile("tiles/[^/]*/[^/]*/[^/]*/([^/]+)/([^/]+)/([^/]+)/.*");

    /**
     * Will parse LocalDates from list of S3ObjectSummaries
     * e.g.
     *  key: tiles/36/M/TD/2017/1/15/0/B01.jp2
     *  date: 2017-01-15
     *
     * @param summaries
     * @return
     */
    public static List<LocalDate> parse(List<S3ObjectSummary> summaries) {
        List<LocalDate> dates = new ArrayList<>();
        while(!summaries.isEmpty()) {
            S3ObjectSummary summary = summaries.get(0);
            Matcher matcher = DATE_PATTERN.matcher(summary.getKey());
            if(matcher.matches()) {
                String y = matcher.group(1);
                String m = matcher.group(2);
                String d = matcher.group(3);
                LocalDate date = LocalDate.of(
                        Integer.valueOf(y),
                        Integer.valueOf(m),
                        Integer.valueOf(d)
                );

                //filter all with the same date
                summaries.removeIf(s -> s.getKey().contains(y+"/"+m+"/"+d));
                dates.add(date);
            } else {
                System.out.println("Found key that doesn't match: " + summary.getKey());
                summaries.removeIf(s -> s.getKey().contains(summary.getKey()));
            }
        }
        return dates;
    }
}

package cz.linkedlist;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import cz.linkedlist.http.xml.Contents;
import cz.linkedlist.http.xml.ListBucketResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class DateParser {

    private static final Logger log = LoggerFactory.getLogger(DateParser.class);
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
    public static List<LocalDate> parseS3(List<S3ObjectSummary> summaries) {
        return parse(summaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList()));
    }

    public static List<LocalDate> parseHttp(ListBucketResult list) {
        return parse(list.getContents().stream().map(Contents::getKey).collect(Collectors.toList()));
    }

    private static List<LocalDate> parse(List<String> keys) {
        List<String> keysCopy = new ArrayList<>(keys);
        List<LocalDate> dates = new ArrayList<>();
        while(!keysCopy.isEmpty()) {
            String key = keysCopy.get(0);
            Matcher matcher = DATE_PATTERN.matcher(key);
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
                keysCopy.removeIf(k -> k.contains(y+"/"+m+"/"+d));
                dates.add(date);
            } else {
                log.info("Found key that doesn't match: " + key);
                keysCopy.removeIf(k -> k.contains(key));
            }
        }
        return dates;
    }
}

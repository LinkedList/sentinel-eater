package cz.linkedlist;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.linkedlist.SentinelEater.BUCKET;
import static cz.linkedlist.SentinelEater.TILES;

/**
 * @author Martin Macko <martin.macko@clevermaps.cz>.
 */
@Service
@RequiredArgsConstructor
public class TileListingService {

    private final AmazonS3Client client;

    public Set<Integer> getYears(UTMCode code) {
        final String prefix = TILES + code.toString();
        ListObjectsV2Request request = buildRequest(prefix);

        ListObjectsV2Result list = client.listObjectsV2(request);
        Set<Integer> years = new HashSet<>();
        for (String s : list.getCommonPrefixes()) {
            years.add(Integer.valueOf(stripPrefixAnsSlash(prefix, s)));
        }
        return years;
    }

    public Set<Integer> getMonths(final UTMCode code, final int year) {
        final String prefix = TILES + code.toString() + year + "/";
        ListObjectsV2Request request = buildRequest(prefix);

        ListObjectsV2Result list = client.listObjectsV2(request);
        Set<Integer> months = new HashSet<>();
        for (String s : list.getCommonPrefixes()) {
            months.add(Integer.valueOf(stripPrefixAnsSlash(prefix, s)));
        }
        return months;

    }

    private ListObjectsV2Request buildRequest(String prefix) {
        ListObjectsV2Request request = new ListObjectsV2Request();
        request.setBucketName(BUCKET);
        request.setPrefix(prefix);
        request.setDelimiter("/");
        return request;
    }

    public Set<Integer> getYears(double latitude, double longitude) {
        UTMCode code = LatLongParser.parse(latitude, longitude);
        return getYears(code);
    }

    /**
     * Returns ordered list of LocalDate for a given latitude and longitude
     * @param latitude
     * @param longitude
     * @return
     */
    public List<LocalDate> squareToDate(double latitude, double longitude) {
        UTMCode code = LatLongParser.parse(latitude, longitude);
        ListObjectsV2Request request1 = new ListObjectsV2Request();
        request1.setBucketName(BUCKET);
        request1.setPrefix(TILES + code.toString());

        ListObjectsV2Result list = client.listObjectsV2(request1);
        List<LocalDate> dates = DateParser.parse(list.getObjectSummaries());
        return dates;
    }

    private static String stripPrefixAnsSlash(String prefix, String value) {
        String strippedPrefix = value.substring(prefix.length());
        String strippedSlash = strippedPrefix.substring(0, strippedPrefix.length() - 1);
        return strippedSlash;
    }
}

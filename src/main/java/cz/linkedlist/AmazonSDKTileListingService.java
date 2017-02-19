package cz.linkedlist;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.linkedlist.SentinelEater.BUCKET;
import static cz.linkedlist.SentinelEater.TILES;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Service
@RequiredArgsConstructor
public class AmazonSDKTileListingService implements TileListingService {

    private final AmazonS3Client client;

    @Override
    public List<String> getFolderContents(TileSet tileSet) {
        ListObjectsV2Request request = buildRequest(tileSet.toString());
        request.setDelimiter(null);
        ListObjectsV2Result list = client.listObjectsV2(request);
        List<String> objects = list.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        return objects;
    }

    @Override
    public boolean exists(final TileSet tileSet) {
        ListObjectsV2Request request = buildRequest(tileSet.toString());
        request.setDelimiter(null);
        ListObjectsV2Result list = client.listObjectsV2(request);
        return list.getKeyCount() > 0;
    }

    @Override
    public Set<Integer> getYears(final UTMCode code) {
        final String prefix = TILES + code.toString();
        return getPossibleValues(prefix);
    }

    @Override
    public Set<Integer> getMonths(final UTMCode code, final int year) {
        final String prefix = TILES + code.toString() + year + "/";
        return getPossibleValues(prefix);
    }

    @Override
    public Set<Integer> getDays(final UTMCode code, final int year, final int month) {
        final String prefix = TILES + code.toString() + year + "/" + month + "/";
        return getPossibleValues(prefix);
    }

    /**
     * Every day can have multiple datasets beginning with 0.
     * Even though there will be usually only single one, we have to check it.
     */
    @Override
    public Set<Integer> getDataSets(final UTMCode code, final int year, final int month, final int day) {
        final String prefix = TILES + code.toString() + year + "/" + month + "/" + day + "/";
        return getPossibleValues(prefix);
    }

    private Set<Integer> getPossibleValues(final String prefix) {
        ListObjectsV2Request request = buildRequest(prefix);
        ListObjectsV2Result list = client.listObjectsV2(request);
        Set<Integer> values = list.getCommonPrefixes().stream()
                .map(s -> Integer.valueOf(stripPrefixAnsSlash(prefix, s)))
                .collect(Collectors.toSet());
        return values;
    }

    private ListObjectsV2Request buildRequest(final String prefix) {
        ListObjectsV2Request request = new ListObjectsV2Request();
        request.setBucketName(BUCKET);
        request.setPrefix(prefix);
        request.setDelimiter("/");
        return request;
    }

    @Override
    public List<LocalDate> availableDates(UTMCode utmCode) {
        ListObjectsV2Request request1 = new ListObjectsV2Request();
        request1.setBucketName(BUCKET);
        request1.setPrefix(TILES + utmCode);
        ListObjectsV2Result list = client.listObjectsV2(request1);
        return DateParser.parse(list.getObjectSummaries());
    }

    private static String stripPrefixAnsSlash(String prefix, String value) {
        String strippedPrefix = value.substring(prefix.length());
        return strippedPrefix.substring(0, strippedPrefix.length() - 1);
    }
}

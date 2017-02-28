package cz.linkedlist.http;

import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.UTMCode;
import cz.linkedlist.http.xml.ListBucketResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("http")
public class HttpTileListingService implements TileListingService {

    private static final String EXISTS_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/?delimiter=/&prefix=";

    private final RestTemplate restTemplate = new RestTemplate();

    public HttpTileListingService() {
        //restTemplate.getMessageConverters().add(new );
    }

    @Override
    public List<String> getFolderContents(TileSet tileSet) {
        return null;
    }

    @Override
    public boolean exists(TileSet tileSet) {
        ListBucketResult result = restTemplate.getForObject(EXISTS_URL + tileSet.toString(), ListBucketResult.class);
        return !result.getCommonPrefixes().isEmpty();
    }

    @Override
    public Set<Integer> getYears(UTMCode code) {
        return null;
    }

    @Override
    public Set<Integer> getMonths(UTMCode code, int year) {
        return null;
    }

    @Override
    public Set<Integer> getDays(UTMCode code, int year, int month) {
        return null;
    }

    @Override
    public Set<Integer> getDataSets(UTMCode code, int year, int month, int day) {
        return null;
    }

    @Override
    public List<LocalDate> availableDates(UTMCode utmCode) {
        return null;
    }
}

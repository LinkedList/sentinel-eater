package cz.linkedlist.http;

import cz.linkedlist.*;
import cz.linkedlist.cache.Cache;
import cz.linkedlist.http.xml.Contents;
import cz.linkedlist.http.xml.ListBucketResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.linkedlist.SentinelEater.TILES;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Service("http-listing")
@Profile(SentinelEater.Profiles.HTTP)
@RequiredArgsConstructor
public class HttpTileListingService implements TileListingService {

    private static final String EXISTS_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/?delimiter=/&prefix=";
    private static final String NO_DELIMITER_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/?prefix=";

    private final RestTemplate restTemplate;
    private final Cache cache;

    @Override
    public List<String> getFolderContents(TileSet tileSet) {
        ListBucketResult result = restTemplate.getForObject(EXISTS_URL + tileSet.toString(), ListBucketResult.class);
        return result.getContents().stream().map(Contents::getKey).collect(Collectors.toList());
    }

    @Override
    public boolean exists(TileSet tileSet) {
        return cache.exists(tileSet).orElseGet(()->{
            ListBucketResult result = restTemplate.getForObject(EXISTS_URL + tileSet.toString(), ListBucketResult.class);
            return cache.insert(tileSet, !result.getContents().isEmpty());
        });
    }

    @Override
    public Set<Integer> getYears(UTMCode code) {
        String prefix = TILES + code.toString();
        return getPossibleValues(prefix);
    }

    @Override
    public Set<Integer> getMonths(UTMCode code, int year) {
        String prefix = TILES + code.toString() + year + "/";
        return getPossibleValues(prefix);
    }

    @Override
    public Set<Integer> getDays(UTMCode code, int year, int month) {
        String prefix = TILES + code.toString() + year + "/" + month + "/";
        return getPossibleValues(prefix);
    }

    @Override
    public Set<Integer> getDataSets(UTMCode code, int year, int month, int day) {
        String prefix = TILES + code.toString() + year + "/" + month + "/" + day + "/";
        return getPossibleValues(prefix);
    }

    @Override
    public List<LocalDate> availableDates(UTMCode utmCode) {
        ListBucketResult result = restTemplate.getForObject(NO_DELIMITER_URL + TILES + utmCode, ListBucketResult.class);
        return DateParser.parseHttp(result);
    }

    @Override
    public Collection<LocalDate> availableDatesAfter(UTMCode utmCode, LocalDate date) {
        final Set<LocalDate> availableDatesAfter = new HashSet<>();
        final Set<Integer> years = this.getYears(utmCode);
        final Set<Integer> possibleYears = years.stream().filter(year -> year > date.getYear()).collect(Collectors.toSet());
        for (Integer year : possibleYears) {
            availableDatesAfter.addAll(availableDates(utmCode, year + "/"));
        }

        // specified date year needs special handling
        final Set<Integer> months = getMonths(utmCode, date.getYear());
        final Set<Integer> possibleMonths = months.stream().filter(m -> m > date.getMonthValue()).collect(Collectors.toSet());
        for(Integer month : possibleMonths) {
            availableDatesAfter.addAll(availableDates(utmCode, date.getYear() + "/" + month + "/"));
        }

        //specified date month needs special handling
        final Set<Integer> days = getDays(utmCode, date.getYear(), date.getMonthValue());
        final Set<Integer> possibleDays = days.stream().filter(d -> d > date.getDayOfMonth()).collect(Collectors.toSet());
        for(Integer day : possibleDays) {
            availableDatesAfter.addAll(availableDates(utmCode, date.getYear() + "/" + date.getMonthValue() + "/" + day + "/"));
        }

        return availableDatesAfter;
    }

    private Set<Integer> getPossibleValues(final String prefix) {
        ListBucketResult result = restTemplate.getForObject(EXISTS_URL + prefix, ListBucketResult.class);
        return result.getCommonPrefixes().stream()
                .map(cp -> Integer.valueOf(stripPrefixAnsSlash(prefix, cp.getPrefix())))
                .collect(Collectors.toSet());
    }

    public List<LocalDate> availableDates(UTMCode utmCode, String prefix) {
        ListBucketResult result = restTemplate.getForObject(NO_DELIMITER_URL + TILES + utmCode + prefix, ListBucketResult.class);
        return DateParser.parseHttp(result);
    }
}

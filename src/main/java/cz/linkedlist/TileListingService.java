package cz.linkedlist;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public interface TileListingService {

    List<String> getFolderContents(TileSet tileSet);

    boolean exists(TileSet tileSet);

    Set<Integer> getYears(UTMCode code);

    Set<Integer> getMonths(UTMCode code, int year);

    Set<Integer> getDays(UTMCode code, int year, int month);

    Set<Integer> getDataSets(UTMCode code, int year, int month, int day);

    List<LocalDate> availableDates(UTMCode utmCode);

    Collection<LocalDate> availableDatesAfter(UTMCode utmCode, LocalDate date);

    default String stripPrefixAnsSlash(String prefix, String value) {
        String strippedPrefix = value.substring(prefix.length());
        return strippedPrefix.substring(0, strippedPrefix.length() - 1);
    }
}

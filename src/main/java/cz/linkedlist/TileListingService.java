package cz.linkedlist;

import java.time.LocalDate;
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

    Set<Integer> getYears(double latitude, double longitude);

    List<LocalDate> availableDates(double latitude, double longitude);

    List<LocalDate> availableDates(UTMCode utmCode);
}

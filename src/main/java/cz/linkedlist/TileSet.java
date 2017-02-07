package cz.linkedlist;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Data
public class TileSet {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d/");

    private final UTMCode code;
    private final LocalDate date;
    private final Integer setOrder;

    public String toString() {
        return "tiles/" + code.toString() + date.format(DATE_FORMAT) + setOrder + "/";
    }

    public Optional<String> getBand(int band) {
        if(band < 1 || band > 12) {
            return Optional.empty();
        } else {
            return Optional.of(toString() + "B" + String.format("%02d", band) + ".jp2");
        }
    }
}

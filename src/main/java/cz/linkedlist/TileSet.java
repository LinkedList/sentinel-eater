package cz.linkedlist;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Data
@RequiredArgsConstructor
public class TileSet {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d/");

    private final UTMCode code;
    private final LocalDate date;
    private final Integer setOrder;

    /**
     * Defaults to set order 0
     * @param code
     * @param date
     */
    public TileSet(UTMCode code, LocalDate date) {
        this(code, date, 0);
    }

    public String toString() {
        return "tiles/" + code.toString() + date.format(DATE_FORMAT) + setOrder + "/";
    }

    public Optional<String> band(int band) {
        if(band < 1 || band > 12) {
            return Optional.empty();
        } else {
            return Optional.of(toString() + "B" + String.format("%02d", band) + ".jp2");
        }
    }

    public String band8A() {
        return toString() + "B8A.jp2";
    }

    public String productInfo() {
        return toString() + "productInfo.json";
    }

    public String tileInfo() {
        return toString() + "tileInfo.json";
    }

    public String metadata() {
        return toString() + "metadata.xml";
    }
}

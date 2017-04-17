package cz.linkedlist;

import cz.linkedlist.info.TileInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Data
@RequiredArgsConstructor
public class TileSet {

    private static final Logger log = LoggerFactory.getLogger(TileSet.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d/");

    private final UTMCode code;
    private final LocalDate date;
    private final Integer setOrder;
    private TileInfo info;

    /**
     * Defaults to set order 0
     * @param code
     * @param date
     */
    public TileSet(UTMCode code, LocalDate date) {
        this(code, date, 0);
    }

    public TileSet(TileSet set) {
        this(set.getCode(), set.getDate(), set.getSetOrder());
    }

    public static TileSet of(String utmCode, int year, int month, int day){
        return new TileSet(UTMCode.of(utmCode), LocalDate.of(year, month, day));
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

    public Double cloudiness() {
        if(info != null) {
            return info.getCloudyPixelPercentage();
        }
        throw new RuntimeException("Get tile cloudiness was attempted for tileSet without TileInfo: " + this.toString());
    }

    public enum Contents {
        BAND_1,
        BAND_2,
        BAND_3,
        BAND_4,
        BAND_5,
        BAND_6,
        BAND_7,
        BAND_8,
        BAND_8A,
        BAND_9,
        BAND_10,
        BAND_11,
        BAND_12,
        PROD_INFO,
        TILE_INFO,
        METADATA
    }

}

package cz.linkedlist;

import lombok.Data;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Data
public class UTMCode {
    /**
     * Grid zone designator
     */
    private final Integer gridZoneDesignator;

    /**
     * Latitude band are lettered C- X (omitting the letters "I" and "O")
     */
    private final String latitudeBand;

    /**
     * Pair of letters designating one of the 100,000 meter side grid squares inside the grid zone
     */
    private final String square;

    /**
     * @return format for S3 bucket searching: gridZoneDesignator/latitudeBand/square/ e.g. 28/C/DG/
     */
    public String toString() {
        return gridZoneDesignator + "/" + latitudeBand + "/" + square + "/";
    }

    public static UTMCode of(String code) {
        Integer gridZone = Integer.valueOf(code.substring(0, 2));
        String band = code.substring(2, 3);
        String square = code.substring(3, 5);

        return new UTMCode(gridZone, band, square);
    }
}

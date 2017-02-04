package cz.linkedlist;

import lombok.Data;

/**
 * @author Martin Macko <martin.macko@clevermaps.cz>.
 */
@Data
public class UTMCode {
    /**
     * Grid zone designator
     */
    private final Integer utm;

    /**
     * Latitude band are lettered C- X (omitting the letters "I" and "O")
     */
    private final String latitudeBand;

    /**
     * Pair of letters designating one of the 100,000 meter side grid squares inside the grid zone
     */
    private final String square;

    /**
     * @return format for S3 bucket searching: utm/latitudeBand/square/ e.g. 28/C/DG/
     */
    public String toString() {
        return utm + "/" + latitudeBand + "/" + square + "/";
    }
}

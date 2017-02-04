package cz.linkedlist;

/**
 * @author Martin Macko <martin.macko@clevermaps.cz>.
 */
public class LatLongParser {

    public static UTMCode parse(double latitude, double longitude) {
        //Return same square for now
        return new UTMCode(36, "M", "TD");
    }

}

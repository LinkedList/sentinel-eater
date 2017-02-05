package cz.linkedlist;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class LatLongParser {

    public static UTMCode parse(double latitude, double longitude) {
        //Return same square for now
        return new UTMCode(36, "M", "TD");
    }

}

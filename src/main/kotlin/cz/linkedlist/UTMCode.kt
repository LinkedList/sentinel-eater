package cz.linkedlist

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
class UTMCode(
    /**
     * Grid zone designator
     */
    val gridZoneDesignator: Int? = null,
    /**
     * Latitude band are lettered C- X (omitting the letters "I" and "O")
     */
    val latitudeBand: String? = null,
    /**
     * Pair of letters designating one of the 100,000 meter side grid squares inside the grid zone
     */
    val square: String? = null
) {

    /**
     * @return format for S3 bucket searching: gridZoneDesignator/latitudeBand/square/ e.g. 28/C/DG/
     */
    override fun toString(): String {
        return gridZoneDesignator.toString() + "/" + latitudeBand + "/" + square + "/"
    }

    companion object {

        fun of(code: String): UTMCode {
            val c: String
            if (code.contains("/")) {
                c = code.replace("/", "")
            } else {
                c = code
            }

            val gridZone = Integer.valueOf(c.substring(0, 2))
            val band = c.substring(2, 3)
            val square = c.substring(3, 5)

            return UTMCode(gridZone, band, square)
        }
    }
}

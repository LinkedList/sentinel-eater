package cz.linkedlist

import cz.linkedlist.info.TileInfo
import org.slf4j.LoggerFactory

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
data class TileSet(
    val code: UTMCode? = null,
    val date: LocalDate? = null,
    val setOrder: Int? = null,
    var info: TileInfo? = null
) {

    /**
     * Defaults to set order 0
     * @param code
     * @param date
     */
    constructor(code: UTMCode, date: LocalDate) : this(code, date, 0)

    override fun toString(): String {
        return "tiles/" + code!!.toString() + date!!.format(DATE_FORMAT) + setOrder + "/"
    }

    fun band(band: Int): Optional<String> {
        return if (band < 1 || band > 12) {
            Optional.empty()
        } else {
            Optional.of(toString() + "B" + String.format("%02d", band) + ".jp2")
        }
    }

    fun band8A(): String {
        return toString() + "B8A.jp2"
    }

    fun productInfo(): String {
        return toString() + "productInfo.json"
    }

    fun tileInfo(): String {
        return toString() + "tileInfo.json"
    }

    fun metadata(): String {
        return toString() + "metadata.xml"
    }

    fun cloudiness(): Double? {
        if (info != null) {
            return info!!.cloudyPixelPercentage
        }
        throw RuntimeException("Get tile cloudiness was attempted for tileSet without TileInfo: " + this.toString())
    }

    enum class Contents {
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
        METADATA;

        fun map(set: TileSet): String {
            return when {
                this.name.matches("BAND_(?:[2-9]|1[0-2]?)".toRegex()) -> set.band(Integer.valueOf(this.name.split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]))
                    .get()
                this == BAND_8A -> set.band8A()
                this == PROD_INFO -> set.productInfo()
                this == TILE_INFO -> set.tileInfo()
                this == METADATA -> set.metadata()
                else -> throw RuntimeException("This shouldn't happen ever!!! Please investigate!!!")
            }

        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(TileSet::class.java)
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d/")

        fun of(utmCode: String, year: Int, month: Int, day: Int): TileSet {
            return TileSet(UTMCode.of(utmCode), LocalDate.of(year, month, day))
        }
    }

}

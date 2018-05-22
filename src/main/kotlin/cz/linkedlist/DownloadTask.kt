package cz.linkedlist

import java.time.LocalDate
import java.util.*

/**
 * Class representing a single continuous checking task
 *
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
data class DownloadTask(
    val utm: UTMCode? = null,
    val cloudiness: Double? = null,
    val date: LocalDate? = null,
    val contents: Array<TileSet.Contents> = emptyArray()) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DownloadTask

        if (utm != other.utm) return false
        if (cloudiness != other.cloudiness) return false
        if (date != other.date) return false
        if (!Arrays.equals(contents, other.contents)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = utm?.hashCode() ?: 0
        result = 31 * result + (cloudiness?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + Arrays.hashCode(contents)
        return result
    }
}

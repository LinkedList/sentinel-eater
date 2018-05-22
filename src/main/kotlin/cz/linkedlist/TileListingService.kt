package cz.linkedlist

import java.time.LocalDate

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
interface TileListingService {

    fun getFolderContents(tileSet: TileSet): List<String>

    fun exists(tileSet: TileSet): Boolean

    fun getYears(code: UTMCode): Set<Int>

    fun getMonths(code: UTMCode, year: Int): Set<Int>

    fun getDays(code: UTMCode, year: Int, month: Int): Set<Int>

    fun getDataSets(code: UTMCode, year: Int, month: Int, day: Int): Set<Int>

    fun availableDates(utmCode: UTMCode): List<LocalDate>

    fun availableDatesAfter(utmCode: UTMCode, date: LocalDate): Collection<LocalDate>

    fun stripPrefixAnsSlash(prefix: String, value: String): String {
        val strippedPrefix = value.substring(prefix.length)
        return strippedPrefix.substring(0, strippedPrefix.length - 1)
    }
}

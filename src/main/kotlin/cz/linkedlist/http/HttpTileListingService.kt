package cz.linkedlist.http

import cz.linkedlist.*
import cz.linkedlist.SentinelEater.Companion.TILES
import cz.linkedlist.cache.Cache
import cz.linkedlist.http.xml.ListBucketResult
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.util.*

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Service("http-listing")
@Profile(SentinelEater.Profiles.HTTP)
@Transactional
open class HttpTileListingService(private val restTemplate: RestTemplate? = null, private val cache: Cache? = null) : TileListingService {

    override fun getFolderContents(tileSet: TileSet): List<String> {
        val result =
            restTemplate!!.getForObject<ListBucketResult>(EXISTS_URL + tileSet.toString(), ListBucketResult::class.java)
        return result.contents.map{ it.key!! }
    }

    override fun exists(tileSet: TileSet): Boolean {
        return cache!!.exists(tileSet).orElseGet {
            val result =
                restTemplate!!.getForObject<ListBucketResult>(
                    EXISTS_URL + tileSet.toString(),
                    ListBucketResult::class.java
                )
            cache.insert(tileSet, !result.contents.isEmpty())
        }
    }

    override fun getYears(code: UTMCode): Set<Int> {
        val prefix = TILES + code.toString()
        return getPossibleValues(prefix)
    }

    override fun getMonths(code: UTMCode, year: Int): Set<Int> {
        val prefix = TILES + code.toString() + year + "/"
        return getPossibleValues(prefix)
    }

    override fun getDays(code: UTMCode, year: Int, month: Int): Set<Int> {
        val prefix = TILES + code.toString() + year + "/" + month + "/"
        return getPossibleValues(prefix)
    }

    override fun getDataSets(code: UTMCode, year: Int, month: Int, day: Int): Set<Int> {
        val prefix = TILES + code.toString() + year + "/" + month + "/" + day + "/"
        return getPossibleValues(prefix)
    }

    override fun availableDates(utmCode: UTMCode): List<LocalDate> {
        val result = restTemplate!!.getForObject<ListBucketResult>(
            NO_DELIMITER_URL + TILES + utmCode,
            ListBucketResult::class.java
        )
        return DateParser.parseHttp(result)
    }

    override fun availableDatesAfter(utmCode: UTMCode, date: LocalDate): Collection<LocalDate> {
        val availableDatesAfter = TreeSet<LocalDate>()
        val years = this.getYears(utmCode)
        val possibleYears =
            years.filter { year -> year > date.year }.toSet()
        for (year in possibleYears) {
            availableDatesAfter.addAll(availableDates(utmCode, year.toString() + "/"))
        }

        // specified date year needs special handling
        val months = getMonths(utmCode, date.year)
        val possibleMonths =
            months.filter { m -> m > date.monthValue }.toSet()
        for (month in possibleMonths) {
            availableDatesAfter.addAll(availableDates(utmCode, date.year.toString() + "/" + month + "/"))
        }

        //specified date month needs special handling
        val days = getDays(utmCode, date.year, date.monthValue)
        val possibleDays = days.filter { d -> d > date.dayOfMonth }.toSet()
        for (day in possibleDays) {
            availableDatesAfter.addAll(
                availableDates(
                    utmCode,
                    date.year.toString() + "/" + date.monthValue + "/" + day + "/"
                )
            )
        }

        return availableDatesAfter
    }

    private fun getPossibleValues(prefix: String): Set<Int> {
        val result = restTemplate!!.getForObject<ListBucketResult>(EXISTS_URL + prefix, ListBucketResult::class.java)
        return result.commonPrefixes
            .map{ Integer.valueOf(stripPrefixAnsSlash(prefix, it.prefix!!)) }
            .toSet()
    }

    fun availableDates(utmCode: UTMCode, prefix: String): List<LocalDate> {
        val result = restTemplate!!.getForObject<ListBucketResult>(
            NO_DELIMITER_URL + TILES + utmCode + prefix,
            ListBucketResult::class.java
        )
        return DateParser.parseHttp(result)
    }

    companion object {

        private val EXISTS_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/?delimiter=/&prefix="
        private val NO_DELIMITER_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/?prefix="
    }
}

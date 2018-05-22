package cz.linkedlist

import com.amazonaws.services.s3.model.S3ObjectSummary
import cz.linkedlist.http.xml.ListBucketResult
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
object DateParser {

    private val log = LoggerFactory.getLogger(DateParser::class.java!!)
    private val DATE_PATTERN = Pattern.compile("tiles/[^/]*/[^/]*/[^/]*/([^/]+)/([^/]+)/([^/]+)/.*")

    /**
     * Will parse LocalDates from list of S3ObjectSummaries
     * e.g.
     * key: tiles/36/M/TD/2017/1/15/0/B01.jp2
     * date: 2017-01-15
     *
     * @param summaries
     * @return
     */
    fun parseS3(summaries: List<S3ObjectSummary>): List<LocalDate> {
        return parse(summaries.map { it.key })
    }

    fun parseHttp(list: ListBucketResult): List<LocalDate> {
        return parse(list.contents.map {it.key!!})
    }

    private fun parse(keys: List<String>): List<LocalDate> {
        val keysCopy = ArrayList(keys)
        val dates = ArrayList<LocalDate>()
        while (!keysCopy.isEmpty()) {
            val key = keysCopy[0]
            val matcher = DATE_PATTERN.matcher(key)
            if (matcher.matches()) {
                val y = matcher.group(1)
                val m = matcher.group(2)
                val d = matcher.group(3)
                val date = LocalDate.of(
                    Integer.valueOf(y),
                    Integer.valueOf(m),
                    Integer.valueOf(d)
                )

                //filter all with the same date
                keysCopy.removeIf { k -> k.contains("$y/$m/$d") }
                dates.add(date)
            } else {
                log.info("Found key that doesn't match: $key")
                keysCopy.removeIf { k -> k.contains(key) }
            }
        }
        return dates
    }
}

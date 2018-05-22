package cz.linkedlist.amazon

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsV2Request
import cz.linkedlist.*
import cz.linkedlist.SentinelEater.Companion.BUCKET
import cz.linkedlist.SentinelEater.Companion.TILES
import cz.linkedlist.cache.Cache
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.stream.Collectors

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
@Service("amazon-listing")
@Profile(SentinelEater.Profiles.AMAZON)
class AmazonSDKTileListingService(
    val client: AmazonS3Client,
    val cache: Cache
) : TileListingService {


    override fun getFolderContents(tileSet: TileSet): List<String> {
        val request = buildRequest(tileSet.toString())
        request.delimiter = null
        val list = client.listObjectsV2(request)
        return list.objectSummaries
            .map { it.key }
    }

    override fun exists(tileSet: TileSet): Boolean {
        return cache.exists(tileSet).orElseGet {
            val request = buildRequest(tileSet.toString())
            request.delimiter = null
            val list = client!!.listObjectsV2(request)
            cache.insert(tileSet, list.keyCount > 0)
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

    /**
     * Every day can have multiple datasets beginning with 0.
     * Even though there will be usually only single one, we have to check it.
     */
    override fun getDataSets(code: UTMCode, year: Int, month: Int, day: Int): Set<Int> {
        val prefix = TILES + code.toString() + year + "/" + month + "/" + day + "/"
        return getPossibleValues(prefix)
    }

    private fun getPossibleValues(prefix: String): Set<Int> {
        val request = buildRequest(prefix)
        val list = client!!.listObjectsV2(request)
        val values = list.commonPrefixes
            .map { s -> Integer.valueOf(stripPrefixAnsSlash(prefix, s)) }
            .toSet()
        return values
    }

    private fun buildRequest(prefix: String): ListObjectsV2Request {
        val request = ListObjectsV2Request()
        request.bucketName = BUCKET
        request.prefix = prefix
        request.delimiter = "/"
        return request
    }

    override fun availableDates(utmCode: UTMCode): List<LocalDate> {
        val request1 = ListObjectsV2Request()
        request1.bucketName = BUCKET
        request1.prefix = TILES + utmCode
        val list = client!!.listObjectsV2(request1)
        return DateParser.parseS3(list.objectSummaries)
    }

    override fun availableDatesAfter(utmCode: UTMCode, date: LocalDate): Collection<LocalDate> {
        throw UnsupportedOperationException("Not yet implemented")
    }
}

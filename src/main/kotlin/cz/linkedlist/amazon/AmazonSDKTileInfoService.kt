package cz.linkedlist.amazon

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.fasterxml.jackson.databind.ObjectMapper
import cz.linkedlist.*
import cz.linkedlist.SentinelEater.Companion.BUCKET
import cz.linkedlist.info.ProductInfo
import cz.linkedlist.info.TileInfo
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.concurrent.ListenableFuture
import java.io.IOException

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Service("amazon-info")
@Profile(SentinelEater.Profiles.AMAZON)
@Transactional
open class AmazonSDKTileInfoService(
    val listingService: TileListingService,
    val mapper: ObjectMapper,
    val client: AmazonS3Client,
    val downInfoService: DownInfoService
) : TileInfoService {


    override fun getTileInfo(tileSet: TileSet): TileInfo {
        return down(tileSet, TileInfo::class.java)
    }

    override fun getProductInfo(tileSet: TileSet): ProductInfo {
        return down(tileSet, ProductInfo::class.java)
    }

    override fun downTileInfo(tileSet: TileSet): ListenableFuture<TileSet> {
        return downInfoService.downTileInfo(tileSet)
    }

    override fun downTileInfo(tileSets: Collection<TileSet>): ListenableFuture<List<TileSet>> {
        val futures = tileSets
            .map{ downInfoService.downTileInfo(it) }
        return AsyncUtil.allOf(futures)
    }

    private fun <OBJ> down(tileSet: TileSet, clazz: Class<OBJ>): OBJ {
        if (!listingService!!.exists(tileSet)) {
            throw RuntimeException("TileSet [$tileSet] doesn't exist. Sorry.")
        }

        val request = GetObjectRequest(BUCKET, key(tileSet, clazz))
        try {
            client!!.getObject(request).use { s3Object ->
                s3Object.objectContent
                    .use({ objectInputStream -> return mapper.readValue<OBJ>(objectInputStream, clazz) })
            }
        } catch (ex: IOException) {
            throw RuntimeException("There was a problem downloading object from bucket", ex)
        }

    }

    companion object {

        fun <OBJ> key(tileSet: TileSet, clazz: Class<OBJ>): String {
            if (TileInfo::class.java == clazz) {
                return tileSet.tileInfo()
            }
            if (ProductInfo::class.java == clazz) {
                return tileSet.productInfo()
            }

            throw RuntimeException(
                "This shouldn't happen. " +
                        "Please check your arguments. clazz should be one of: TileInfo, ProductInfo. Instead supplied: " + clazz
            )
        }
    }
}

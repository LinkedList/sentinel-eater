package cz.linkedlist.amazon

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import cz.linkedlist.AbstractTileDownloader
import cz.linkedlist.SentinelEater
import cz.linkedlist.SentinelEater.Companion.BUCKET
import cz.linkedlist.TileListingService
import cz.linkedlist.TileSet
import org.apache.commons.io.IOUtils
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import mu.KLogging

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Service("amazon-downloader")
@Async
@Profile(SentinelEater.Profiles.AMAZON)
open class AmazonSDKTileDownloader(
    val client: AmazonS3Client,
    val listingService: TileListingService
) : AbstractTileDownloader() {

    companion object: KLogging()


    public override fun down(tileSet: TileSet, what: String): File {
        if (!listingService!!.exists(tileSet)) {
            throw RuntimeException("I cannot download something, that doesn't exist, sorry. TileSet: $tileSet")
        }

        ensureFolderExists(destinationFolder)
        val downloaded = isDownloaded(what)
        if (downloaded != null) {
            logger.info("File [{}] already exists, don't have to download again.", downloaded)
            return downloaded
        }

        val request = GetObjectRequest(BUCKET, what)
        val file = File(destinationFolder + what.replace("/", "_"))
        try {
            client!!.getObject(request).use { s3Object ->
                FileOutputStream(file).use { fos ->
                    s3Object.objectContent.use({ objectInputStream ->
                        IOUtils.copy(objectInputStream, fos)
                        return file
                    })
                }
            }
        } catch (ex: IOException) {
            throw RuntimeException("There was a problem downloading object from bucket", ex)
        }

    }

}

package cz.linkedlist.http

import cz.linkedlist.*
import mu.KLogging
import org.apache.commons.io.FileUtils
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import java.io.File
import java.io.IOException
import java.net.URL

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
@Service("http-downloader")
@Async
@Profile(SentinelEater.Profiles.HTTP)
open class HttpTileDownloader(
    protected var listingService: TileListingService
) : AbstractTileDownloader() {

    companion object: KLogging()

    public override fun down(tileSet: TileSet, what: String): File {
        if (!listingService.exists(tileSet)) {
            throw RuntimeException("I cannot download something, that doesn't exist, sorry. TileSet: $tileSet")
        }
        ensureFolderExists(destinationFolder)
        val downloaded = isDownloaded(what)
        if (downloaded != null) {
            logger.info("File [{}] already exists, don't have to download again.", downloaded)
            return downloaded
        }

        val file = File(destinationFolder + what.replace("/", "_"))

        try {
            FileUtils.copyURLToFile(URL(TileDownloader.DOWN_URL + what), file)
        } catch (O_o: IOException) {
            throw RuntimeException("There was a problem downloading object from bucket", O_o)
        }

        return file
    }
}

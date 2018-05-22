package cz.linkedlist

import java.io.File

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
interface TileDownloader {

    fun downBand(tileSet: TileSet, band: Int)

    fun downBand8A(tileSet: TileSet)

    fun downProductInfo(tileSet: TileSet)

    fun downTileInfo(tileSet: TileSet)

    fun downMetadata(tileSet: TileSet)

    fun downContent(tileSet: TileSet, content: TileSet.Contents)

    fun downContent(tileSet: TileSet, contents: Array<TileSet.Contents>)

    fun ensureFolderExists(destinationFolder: String) {
        val destFolder = File(destinationFolder)
        if (destFolder.exists() && destFolder.isDirectory) {
            return
        }

        val success = destFolder.mkdirs()
        if (success) {
            return
        }

        throw RuntimeException("Cannot create necessary folder for downloading: $destFolder")
    }

    fun isDownloaded(what: String): File?

    companion object {

        const val DEFAULT_FOLDER = "/tmp/sentinel/"
        const val DESTINATION_FOLDER_PROP = "\${destinationFolder:$DEFAULT_FOLDER}"
        const val DOWN_URL = "https://sentinel-s2-l1c.s3.amazonaws.com/"
    }

}

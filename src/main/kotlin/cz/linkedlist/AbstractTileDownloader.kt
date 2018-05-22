package cz.linkedlist

import org.springframework.beans.factory.annotation.Value

import java.io.File

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
abstract class AbstractTileDownloader : TileDownloader {
    @Value(TileDownloader.DESTINATION_FOLDER_PROP)
    lateinit var destinationFolder: String


    override fun downBand(tileSet: TileSet, band: Int) {
        tileSet.band(band)
            .map { s -> down(tileSet, s) }
            .orElseThrow { RuntimeException("I am sorry, cannot download band: $band") }
    }

    override fun downBand8A(tileSet: TileSet) {
        down(tileSet, tileSet.band8A())
    }

    override fun downProductInfo(tileSet: TileSet) {
        down(tileSet, tileSet.productInfo())
    }

    override fun downTileInfo(tileSet: TileSet) {
        down(tileSet, tileSet.tileInfo())
    }

    override fun downMetadata(tileSet: TileSet) {
        down(tileSet, tileSet.metadata())
    }

    override fun downContent(tileSet: TileSet, content: TileSet.Contents) {
        this.down(tileSet, content.map(tileSet))
    }

    override fun downContent(tileSet: TileSet, contents: Array<TileSet.Contents>) {
        contents.forEach { c -> this.downContent(tileSet, c) }
    }

    protected abstract fun down(tileSet: TileSet, what: String): File


    override fun isDownloaded(what: String): File? {
        val file = File(destinationFolder + what.replace("/", "_"))
        return if (file.exists()) file else null
    }
}

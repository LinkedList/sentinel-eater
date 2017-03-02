package cz.linkedlist;

import java.io.File;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public interface TileDownloader {

    String DESTINATION_FOLDER_PROP = "${destinationFolder}";

    void downBand(TileSet tileSet, int band);

    void downBand8A(TileSet tileSet);

    void downProductInfo(TileSet tileSet);

    void downTileInfo(TileSet tileSet);

    void downMetadata(TileSet tileSet);

    default void ensureFolderExists(String destinationFolder) {
        File destFolder = new File(destinationFolder);
        if (destFolder.exists() && destFolder.isDirectory()) {
            return;
        }

        final boolean success = destFolder.mkdirs();
        if(success) {
            return;
        }

        throw new RuntimeException("Cannot create necessary folder for downloading: " + destFolder);
    }

}

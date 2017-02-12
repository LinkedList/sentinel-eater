package cz.linkedlist;

import org.springframework.core.io.Resource;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class UTMGeometryMap {

    private Map<Rectangle2D, UTMCode> map = new HashMap<>();
    private Map<UTMCode, Rectangle2D> inverseMap = new HashMap<>();

    /**
     * Will parse csv file with utm codes and geometries and create a map of <Rectangle2D, UTMCode>
     * File is in this format:
     *  name;x;y;area;width;height;st_asgeojson
     * @param utmTilesFile
     * @throws IOException
     */
    public UTMGeometryMap(Resource utmTilesFile) throws IOException {
        Files.lines(utmTilesFile.getFile().toPath()).skip(1).forEach(s -> {
            String[] line = s.split(";");
            UTMCode code = UTMCode.of(line[0]);

            Rectangle2D tile = new Rectangle2D.Double(
                    Double.valueOf(line[1]),
                    Double.valueOf(line[2]),
                    Double.valueOf(line[4]),
                    Double.valueOf(line[5])
            );

            map.put(tile, code);
            inverseMap.put(code, tile);
        });
    }

    public Rectangle2D get(UTMCode code) {
        return inverseMap.get(code);
    }

    public UTMCode get(Rectangle2D rect) {
        return map.get(rect);
    }
}

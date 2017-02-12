package cz.linkedlist;

import org.springframework.core.io.Resource;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<UTMCode> intersects(double x, double y) {
        Point2D point = new Point2D.Double(x, y);
        Line2D line = new Line2D.Double(point, point);
        return map.entrySet().parallelStream()
                .filter(e -> e.getKey().intersectsLine(line))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public Set<UTMCode> intersects(double x, double y, double w, double h) {
        Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);
        return map.entrySet().parallelStream()
                .filter(e -> e.getKey().intersects(rect))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}

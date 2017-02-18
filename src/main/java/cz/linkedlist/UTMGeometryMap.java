package cz.linkedlist;

import com.vividsolutions.jts.geom.*;
import org.springframework.core.io.Resource;
import org.wololo.jts2geojson.GeoJSONReader;

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

    private Map<Geometry, UTMCode> map = new HashMap<>();
    private Map<UTMCode, Geometry> inverseMap = new HashMap<>();
    private GeometryFactory geometryFactory = new GeometryFactory();
    private GeoJSONReader geoJSONReader = new GeoJSONReader();

    /**
     * Will parse csv file with utm codes and geometries and create a map of <Rectangle2D, UTMCode>
     * File is in this format:
     *  name;st_asgeojson
     * @param utmTilesFile
     * @throws IOException
     */
    public UTMGeometryMap(Resource utmTilesFile) throws IOException {

        Files.lines(utmTilesFile.getFile().toPath()).skip(1).forEach(s -> {
            String[] line = s.split(";");
            UTMCode code = UTMCode.of(line[0]);
            Geometry geom = geoJSONReader.read(line[1]);

            map.put(geom, code);
            inverseMap.put(code, geom);
        });
    }

    public Set<UTMCode> intersects(double x, double y) {
        Point point = geometryFactory.createPoint(new Coordinate(x, y));
        return map.entrySet().parallelStream()
                .filter(e -> e.getKey().intersects(point))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}

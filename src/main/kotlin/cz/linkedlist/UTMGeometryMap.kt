package cz.linkedlist

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.wololo.jts2geojson.GeoJSONReader

import java.io.IOException
import java.nio.file.Files
import java.util.HashMap
import java.util.stream.Collectors

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Component
class UTMGeometryMap
/**
 * Will parse csv file with utm codes and geometries and create a map of <Rectangle2D></Rectangle2D>, UTMCode>
 * File is in this format:
 * name;st_asgeojson
 * @param utmTilesFile
 * @throws IOException
 */
@Throws(IOException::class)
constructor(@Value("utm-tiles.csv") utmTilesFile: Resource) {

    private val map = HashMap<Geometry, UTMCode>()
    private val inverseMap = HashMap<UTMCode, Geometry>()
    private val geometryFactory = GeometryFactory()
    private val geoJSONReader = GeoJSONReader()

    init {
        Files.lines(utmTilesFile.file.toPath()).skip(1).forEach { s ->
            val line = s.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val code = UTMCode.of(line[0])
            val geom = geoJSONReader.read(line[1])

            map[geom] = code
            inverseMap[code] = geom
        }
    }

    fun intersects(x: Double, y: Double): Set<UTMCode> {
        val point = geometryFactory.createPoint(Coordinate(x, y))
        return map.entries
            .filter { it.key.intersects(point) }
            .map { it.value }
            .toSet()
    }
}

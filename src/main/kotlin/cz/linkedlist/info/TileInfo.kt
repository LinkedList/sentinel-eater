package cz.linkedlist.info

import com.vividsolutions.jts.geom.Geometry

import java.sql.Timestamp

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
data class TileInfo(
    val path: String? = null,
    val timestamp: Timestamp? = null,
    val utmZone: Int? = null,
    val latitudeBand: String? = null,
    val gridSquare: String? = null,
    val datastrip: DataStrip? = null,
    val tileGeometry: Geometry? = null,
    val tileDataGeometry: Geometry? = null,
    val tileOrigin: Geometry? = null,
    val dataCoveragePercentage: Double? = null,
    val cloudyPixelPercentage: Double? = null,
    val productName: String? = null,
    val productPath: String? = null
)

/*
{
  "path" : "tiles/15/R/TM/2017/1/14/0",
  "timestamp" : "2017-01-14T17:02:05.373Z",
  "utmZone" : 15,
  "latitudeBand" : "R",
  "gridSquare" : "TM",
  "datastrip" : {
    "id" : "S2A_OPER_MSI_L1C_DS_MTI__20170114T215833_S20170114T170205_N02.04",
    "path" : "products/2017/1/14/S2A_MSIL1C_20170114T165631_N0204_R026_T15RTM_20170114T170205/datastrip/0"
  },
  "tileGeometry" : {
    "type" : "Polygon",
    "crs" : {
      "type" : "name",
      "properties" : {
        "name" : "urn:ogc:def:crs:EPSG:8.8.1:32615"
      }
    },
    "coordinates" : [ [ [ 199980.0, 3200040.0 ], [ 309780.0, 3200040.0 ], [ 309780.0, 3090240.0 ], [ 199980.0, 3090240.0 ], [ 199980.0, 3200040.0 ] ] ]
  },
  "tileDataGeometry" : {
    "type" : "Polygon",
    "crs" : {
      "type" : "name",
      "properties" : {
        "name" : "urn:ogc:def:crs:EPSG:8.8.1:32615"
      }
    },
    "coordinates" : [ [ [ 237601.970223567, 3200039.0 ], [ 309779.0, 3200039.0 ], [ 309779.0, 3090241.0 ], [ 210626.601048234, 3090241.0 ], [ 237601.970223567, 3200039.0 ] ] ]
  },
  "tileOrigin" : {
    "type" : "Point",
    "crs" : {
      "type" : "name",
      "properties" : {
        "name" : "urn:ogc:def:crs:EPSG:8.8.1:32615"
      }
    },
    "coordinates" : [ 199980.0, 3200040.0 ]
  },
  "dataCoveragePercentage" : 78.02,
  "cloudyPixelPercentage" : 87.73,
  "productName" : "S2A_MSIL1C_20170114T165631_N0204_R026_T15RTM_20170114T170205",
  "productPath" : "products/2017/1/14/S2A_MSIL1C_20170114T165631_N0204_R026_T15RTM_20170114T170205"
}
 */
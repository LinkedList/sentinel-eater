package cz.linkedlist.info

import java.sql.Timestamp

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
data class ProductInfo(
    private val name: String? = null,
    private val id: String? = null,
    private val path: String? = null,
    private val timestamp: Timestamp? = null,
    private val datatakeIdentifier: String? = null,
    private val sciHubIngestion: Timestamp? = null,
    private val s3Ingestion: Timestamp? = null,
    private val tiles: List<TileInfo>? = null,
    private val datastrips: List<DataStrip>? = null
)

/*
{
  "name" : "S2A_MSIL1C_20170302T164311_N0204_R126_T15PUS_20170302T165848",
  "id" : "936a7177-6db4-4adc-93af-4817bf8b55ee",
  "path" : "products/2017/3/2/S2A_MSIL1C_20170302T164311_N0204_R126_T15PUS_20170302T165848",
  "timestamp" : "2017-03-02T16:43:11.026Z",
  "datatakeIdentifier" : "GS2A_20170302T164311_008846_N02.04",
  "sciHubIngestion" : "2017-03-03T00:32:42.666Z",
  "s3Ingestion" : "2017-03-05T13:41:29.945Z",
  "tiles" : [ {
    "path" : "tiles/15/P/US/2017/3/2/0",
    "timestamp" : "2017-03-02T16:58:48.477Z",
    "utmZone" : 15,
    "latitudeBand" : "P",
    "gridSquare" : "US",
    "datastrip" : {
      "id" : "S2A_OPER_MSI_L1C_DS_SGS__20170302T233256_S20170302T165848_N02.04",
      "path" : "products/2017/3/2/S2A_MSIL1C_20170302T164311_N0204_R126_T15PUS_20170302T165848/datastrip/0"
    }
  } ],
  "datastrips" : [ {
    "id" : "S2A_OPER_MSI_L1C_DS_SGS__20170302T233256_S20170302T165848_N02.04",
    "path" : "products/2017/3/2/S2A_MSIL1C_20170302T164311_N0204_R126_T15PUS_20170302T165848/datastrip/0"
  } ]
}
 */

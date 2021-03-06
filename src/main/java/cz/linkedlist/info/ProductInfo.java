package cz.linkedlist.info;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Data
public class ProductInfo {
    private String name;
    private String id;
    private String path;
    private Timestamp timestamp;
    private String datatakeIdentifier;
    private Timestamp sciHubIngestion;
    private Timestamp s3Ingestion;
    private List<TileInfo> tiles;
    private List<DataStrip> datastrips;
}

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

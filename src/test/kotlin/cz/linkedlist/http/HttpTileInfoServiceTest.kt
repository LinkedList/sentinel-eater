package cz.linkedlist.http

import cz.linkedlist.TileInfoService
import cz.linkedlist.TileSet
import cz.linkedlist.UTMCode
import cz.linkedlist.info.ProductInfo
import cz.linkedlist.info.TileInfo
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.concurrent.ListenableFuture

import java.time.LocalDate
import java.util.Arrays

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */

@SpringBootTest
@RunWith(SpringRunner::class)
class HttpTileInfoServiceTest {

    @Autowired
    private val service: TileInfoService? = null
    @Autowired
    private val jdbc: JdbcTemplate? = null

    @Before
    fun init() {
        jdbc!!.execute("TRUNCATE TABLE cache")
    }

    @Test
    fun downloadTileInfo() {
        val (path, _, utmZone, latitudeBand, gridSquare, _, _, _, _, _, cloudyPixelPercentage) = service!!.getTileInfo(
            TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        )
        assertThat<Int>(utmZone, `is`(36))
        assertThat<String>(latitudeBand, `is`("M"))
        assertThat<String>(gridSquare, `is`("TD"))
        assertThat<Double>(cloudyPixelPercentage, `is`(0.06))
        assertThat<String>(path, `is`("tiles/36/M/TD/2016/8/31/0"))
    }

    @Test
    fun downloadProductInfo() {
        val productInfo = service!!.getProductInfo(TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31)))
        assertThat(
            productInfo.name,
            `is`("S2A_OPER_PRD_MSIL1C_PDMC_20160831T150507_R078_V20160831T080612_20160831T082520")
        )
    }

    @Test
    @Ignore("TODO Ignore till I figure out how to make junit taskExecutor not reject tasks")
    fun testDownloadTileInfoAndSetToTileSet() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val f = service!!.downTileInfo(tileSet)
        f.addCallback({ set ->
            assertThat(set, not(tileSet))
            assertThat<TileInfo>(tileSet.info, not(nullValue()))
            assertThat<Double>(tileSet.cloudiness(), `is`(0.06))
        }) { t -> Assert.fail() }
    }

    @Test
    fun testDownloadTileInfoAndSetToTileSets() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val tileSet2 = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val f = service!!.downTileInfo(Arrays.asList(tileSet, tileSet2))
        f.addCallback(
            { list ->
                assertThat(list, hasSize(2))
                list.forEach { set ->
                    assertThat<TileInfo>(set.info, not(nullValue()))
                    assertThat<Double>(set.cloudiness(), closeTo(0.06, 0.0))
                }
            }) { t -> Assert.fail() }
    }

    @Test
    fun testDownloadTileInfo() {
        val tileSet = TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31))
        val f = service!!.downTileInfo(tileSet)
        f.addCallback({ set ->
            assertThat<TileInfo>(set.info, not(nullValue()))
            assertThat<Double>(set.cloudiness(), closeTo(0.06, 0.0))
        }) { throwable -> Assert.fail() }
    }

    @Test(expected = RuntimeException::class)
    fun downloadNotExists() {
        service!!.getTileInfo(TileSet(UTMCode.of("11AAA"), LocalDate.of(2016, 8, 31)))
    }

    @Test(expected = RuntimeException::class)
    fun downloadNotExistsProductInfo() {
        service!!.getProductInfo(TileSet(UTMCode.of("11AAA"), LocalDate.of(2016, 8, 31)))
    }
}

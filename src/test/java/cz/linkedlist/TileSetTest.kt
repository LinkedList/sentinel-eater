package cz.linkedlist

import cz.linkedlist.info.TileInfo
import org.junit.Test

import java.time.LocalDate
import java.util.Optional

import cz.linkedlist.TileSet.Contents.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.sameInstance

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
class TileSetTest {

    @Test
    @Throws(Exception::class)
    fun testToString() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 10)
        val setOrder = 0

        val tileSet = TileSet(code, date, setOrder)

        assertThat(tileSet.toString(), `is`("tiles/28/C/DD/2017/3/10/0/"))
    }

    @Test
    @Throws(Exception::class)
    fun testDefaultBand() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 10)

        val tileSet = TileSet(code, date)

        assertThat(tileSet.toString(), `is`("tiles/28/C/DD/2017/3/10/0/"))
    }

    @Test
    @Throws(Exception::class)
    fun testToStringsingleDay() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 9)
        val setOrder = 0

        val tileSet = TileSet(code, date, setOrder)

        assertThat(tileSet.toString(), `is`("tiles/28/C/DD/2017/3/9/0/"))
    }

    @Test
    fun getBandTest() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 9)
        val setOrder = 0
        val tileSet = TileSet(code, date, setOrder)

        assertThat(tileSet.band(0), `is`<Optional<out Any>>(Optional.empty()))
        assertThat(tileSet.band(13), `is`<Optional<out Any>>(Optional.empty()))
        assertThat(tileSet.band(2).get(), `is`(tileSet.toString() + "B02.jp2"))
        assertThat(tileSet.band(11).get(), `is`(tileSet.toString() + "B11.jp2"))
        assertThat(tileSet.band8A(), `is`(tileSet.toString() + "B8A.jp2"))
    }

    @Test
    fun getOtherFilesTest() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 9)
        val setOrder = 0
        val tileSet = TileSet(code, date, setOrder)

        assertThat(tileSet.productInfo(), `is`(tileSet.toString() + "productInfo.json"))
        assertThat(tileSet.metadata(), `is`(tileSet.toString() + "metadata.xml"))
        assertThat(tileSet.tileInfo(), `is`(tileSet.toString() + "tileInfo.json"))
    }

    @Test
    fun getNewInstance() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 9)
        val setOrder = 0
        val tileSet = TileSet(code, date, setOrder)
        val clone = TileSet(tileSet)

        assertThat(tileSet, not(sameInstance(clone)))
        assertThat(tileSet, `is`(clone))
    }

    @Test(expected = RuntimeException::class)
    fun getCloudinessWithout() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 9)
        val setOrder = 0
        val tileSet = TileSet(code, date, setOrder)
        tileSet.cloudiness()
    }

    @Test
    fun getCloudinessWith() {
        val code = UTMCode(28, "C", "DD")
        val date = LocalDate.of(2017, 3, 9)
        val setOrder = 0
        val tileSet = TileSet(code, date, setOrder)
        val info = TileInfo(cloudyPixelPercentage = 50.0)
        tileSet.info = info

        assertThat<Double>(tileSet.cloudiness(), `is`(50.0))
    }

    @Test
    fun testContentsMap() {
        val tileSet = TileSet.of("28CDD", 2017, 3, 10)

        assertThat(BAND_1.map(tileSet), `is`(tileSet.band(1).get()))
        assertThat(BAND_2.map(tileSet), `is`(tileSet.band(2).get()))
        assertThat(BAND_3.map(tileSet), `is`(tileSet.band(3).get()))
        assertThat(BAND_4.map(tileSet), `is`(tileSet.band(4).get()))
        assertThat(BAND_5.map(tileSet), `is`(tileSet.band(5).get()))
        assertThat(BAND_6.map(tileSet), `is`(tileSet.band(6).get()))
        assertThat(BAND_7.map(tileSet), `is`(tileSet.band(7).get()))
        assertThat(BAND_8.map(tileSet), `is`(tileSet.band(8).get()))
        assertThat(BAND_9.map(tileSet), `is`(tileSet.band(9).get()))
        assertThat(BAND_10.map(tileSet), `is`(tileSet.band(10).get()))
        assertThat(BAND_11.map(tileSet), `is`(tileSet.band(11).get()))
        assertThat(BAND_12.map(tileSet), `is`(tileSet.band(12).get()))
        assertThat(BAND_8A.map(tileSet), `is`(tileSet.band8A()))
        assertThat(PROD_INFO.map(tileSet), `is`(tileSet.productInfo()))
        assertThat(TILE_INFO.map(tileSet), `is`(tileSet.tileInfo()))
        assertThat(METADATA.map(tileSet), `is`(tileSet.metadata()))
    }

}
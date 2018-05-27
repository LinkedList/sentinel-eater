package cz.linkedlist

import org.junit.Before
import org.junit.Test
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource

import java.io.IOException

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
class UTMGeometryMapTest {

    private var utmMap: UTMGeometryMap? = null

    @Before
    @Throws(IOException::class)
    fun before() {
        val classLoader = javaClass.classLoader
        val csvFileResource = UrlResource(classLoader.getResource("utm-tiles.csv")!!)
        utmMap = UTMGeometryMap(csvFileResource)
    }

    @Test
    fun testPointIntersects() {
        val set = utmMap!!.intersects(-15.49, 40.22)
        assertThat(set, hasItem(UTMCode.of("28TDK")))
        assertThat(set, hasSize(1))
    }

}
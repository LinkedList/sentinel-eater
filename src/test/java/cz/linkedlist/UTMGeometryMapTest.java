package cz.linkedlist;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class UTMGeometryMapTest {

    private UTMGeometryMap utmMap;

    @BeforeTest
    public void before() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Resource csvFileResource = new UrlResource(classLoader.getResource("utm-tiles.csv"));
        utmMap = new UTMGeometryMap(csvFileResource);
    }

    @Test
    public void testParsingOfCSV() throws IOException {
        assertThat(utmMap.get(UTMCode.of("28SFH")), not(nullValue()));
    }

    @Test
    public void getByCode() {
        assertThat(utmMap.get(UTMCode.of("28SFH")).getX(), is(-13.8632824087));
        assertThat(utmMap.get(UTMCode.of("28SFH")).getY(), is(38.8433146054));
        assertThat(utmMap.get(UTMCode.of("28SFH")).getWidth(), is(1.2800146119));
        assertThat(utmMap.get(UTMCode.of("28SFH")).getHeight(), is(1.0080124955));
    }

    @Test
    public void getByRect() {
        Rectangle2D rect = new Rectangle2D.Double(-13.8632824087, 38.8433146054, 1.2800146119, 1.0080124955);
        assertThat(utmMap.get(rect), is(UTMCode.of("28SFH")));
    }
}
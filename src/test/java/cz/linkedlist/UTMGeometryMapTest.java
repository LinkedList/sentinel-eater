package cz.linkedlist;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class UTMGeometryMapTest {

    private UTMGeometryMap utmMap;

    @Before
    public void before() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Resource csvFileResource = new UrlResource(classLoader.getResource("utm-tiles.csv"));
        utmMap = new UTMGeometryMap(csvFileResource);
    }

    @Test
    public void testPointIntersects() {
        Set<UTMCode> set = utmMap.intersects(-15.49, 40.22);
        assertThat(set, hasItem(UTMCode.of("28TDK")));
        assertThat(set, hasSize(1));
    }

}
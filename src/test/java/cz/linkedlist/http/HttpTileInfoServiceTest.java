package cz.linkedlist.http;

import cz.linkedlist.config.SentinelEaterTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@SentinelEaterTest
public class HttpTileInfoServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private HttpTileInfoService service;

    @Test
    public void testGet() throws Exception {

    }

}
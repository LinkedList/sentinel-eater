package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.linkedlist.TileListingService;
import cz.linkedlist.TileSet;
import cz.linkedlist.config.JacksonConfiguration;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class AmazonSDKTileInfoServiceTest {

    @Mock
    private TileListingService listingService;
    @Mock
    private AmazonS3Client client;

    private AmazonSDKTileInfoService service;

    private Resource productInfoResource;
    private Resource tileInfoResource;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ClassLoader classLoader = getClass().getClassLoader();
        productInfoResource = new UrlResource(classLoader.getResource("productInfo.json"));
        tileInfoResource = new UrlResource(classLoader.getResource("tileInfo.json"));

        service = new AmazonSDKTileInfoService();
        ReflectionTestUtils.setField(service, "client", client);
        ReflectionTestUtils.setField(service, "listingService", listingService);
        JacksonConfiguration jacksonConfiguration= new JacksonConfiguration();
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = jacksonConfiguration.jackson2ObjectMapperBuilder();
        ObjectMapper mapper = jackson2ObjectMapperBuilder.build();
        ReflectionTestUtils.setField(service, "mapper", mapper);
    }

    @Test(expected = RuntimeException.class)
    public void testGetProductInfoNotExists() {
        when(listingService.exists(any())).thenReturn(false);
        service.getTileInfo(TileSet.of("36MTD", 2016, 8, 31));
    }

    @Test
    public void testGetProductInfo() throws Exception {
        when(listingService.exists(any())).thenReturn(true);
        TileSet set = TileSet.of("36MTD", 2016, 8, 31);
        S3Object object = mock(S3Object.class);
        when(object.getObjectContent()).thenReturn(new S3ObjectInputStream(productInfoResource.getInputStream(), mock(HttpRequestBase.class)));

        when(client.getObject(any())).thenReturn(object);
        ProductInfo productInfo = service.getProductInfo(set);
        assertThat(productInfo, not(nullValue()));
        assertThat(productInfo.getId(), not(nullValue()));
    }

    @Test
    public void testGetTileInfo() throws Exception {
        when(listingService.exists(any())).thenReturn(true);
        TileSet set = TileSet.of("36MTD", 2016, 8, 31);
        S3Object object = mock(S3Object.class);
        when(object.getObjectContent()).thenReturn(new S3ObjectInputStream(tileInfoResource.getInputStream(), mock(HttpRequestBase.class)));

        when(client.getObject(any())).thenReturn(object);
        TileInfo tileInfo = service.getTileInfo(set);
        assertThat(tileInfo, not(nullValue()));
        assertThat(tileInfo.getPath(), not(nullValue()));
    }

}
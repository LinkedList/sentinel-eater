package cz.linkedlist.amazon;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.linkedlist.*;
import cz.linkedlist.config.JacksonConfiguration;
import cz.linkedlist.info.ProductInfo;
import cz.linkedlist.info.TileInfo;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    private DownInfoService downInfoService = new DownInfoServiceImpl();

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
        ReflectionTestUtils.setField(downInfoService, "service", service);
        ReflectionTestUtils.setField(service, "downInfoService", downInfoService);
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

    @Test
    public void testDownloadTileInfoAndSetToTileSets() throws Exception {
        when(listingService.exists(any())).thenReturn(true);
        S3Object object = mock(S3Object.class);
        when(object.getObjectContent()).thenReturn(
                new S3ObjectInputStream(tileInfoResource.getInputStream(), mock(HttpRequestBase.class)),
                new S3ObjectInputStream(tileInfoResource.getInputStream(), mock(HttpRequestBase.class))
        );
        when(client.getObject(any())).thenReturn(object);
        final TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        final TileSet tileSet2 = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        final ListenableFuture<List<TileSet>> f = service.downTileInfo(Arrays.asList(tileSet, tileSet2));
        f.addCallback(
                list -> {
                    assertThat(list, hasSize(2));
                    list.forEach(set -> {
                        assertThat(set.getInfo(), not(nullValue()));
                        assertThat(set.cloudiness(), closeTo(87.73D, 0D));
                    });
                }, t -> Assert.fail());
    }

    @Test
    public void testKey() {
        final TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        assertThat(AmazonSDKTileInfoService.key(tileSet, TileInfo.class), is(tileSet.tileInfo()));
        assertThat(AmazonSDKTileInfoService.key(tileSet, ProductInfo.class), is(tileSet.productInfo()));
    }

    @Test(expected = RuntimeException.class)
    public void testKeyNotExistentClass() {
        final TileSet tileSet = new TileSet(UTMCode.of("36MTD"), LocalDate.of(2016, 8, 31));
        AmazonSDKTileInfoService.key(tileSet, Object.class);
    }
}
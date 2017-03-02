package cz.linkedlist;

import cz.linkedlist.http.HttpTileDownloaderImpl;
import cz.linkedlist.http.HttpTileListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
@EnableAutoConfiguration
public class SentinelEater implements CommandLineRunner {

	public static final String BUCKET = "sentinel-s2-l1c";
	public static final String TILES = "tiles/";

//	@Autowired
//	private ResourcePatternResolver resourcePatternResolver;

	@Autowired
	private UTMGeometryMap utmGeometryMap;

    @Autowired
	private HttpTileListingService tileListingService;

	@Autowired
	private HttpTileDownloaderImpl tileDownloader;

	public static void main(String[] args) {
		SpringApplication.run(SentinelEater.class, args).close();
	}

	@Override
	public void run(String... strings) throws Exception {
//		Set<Integer> years = tileListingService.getYears(1.1, 2.2);
//		System.out.println(years);

//		List<LocalDate> dates = tileListingService.availableDates(1.1, 2.2);
//		System.out.println(dates);

//		Resource[] allFiles =  this.resourcePatternResolver.getResources("s3://"+BUCKET+"/tiles/36/M/TD/2017/1/**/*");
//		for (Resource resource : allFiles) {
//			System.out.println(resource.getFilename());
//		}

		TileSet tileSet = new TileSet(new UTMCode(36,"M", "TD"), LocalDate.of(2016, 8, 31), 0);
//		System.out.println(tileListingService.exists(tileSet));
//		System.out.println(tileListingService.getFolderContents(tileSet));
		tileDownloader.downBand(tileSet, 1);
//		tileDownloader.downProductInfo(tileSet);
//		tileDownloader.downMetadata(tileSet);
//		tileDownloader.downTileInfo(tileSet);
//		tileListingService.availableDates(tileSet.getCode());

//		Set<UTMCode> intersects = utmGeometryMap.intersects(-15.49, 40.22);
//		System.out.println(intersects);
	}
}

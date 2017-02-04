package cz.linkedlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
@EnableAutoConfiguration
public class SentinelEater implements CommandLineRunner {

	public static final String BUCKET = "sentinel-s2-l1c";
	public static final String TILES = "tiles/";

//	@Autowired
//	private ResourcePatternResolver resourcePatternResolver;

    @Autowired
	private TileListingService tileListingService;

	public static void main(String[] args) {
		SpringApplication.run(SentinelEater.class, args).close();
	}

	@Override
	public void run(String... strings) throws Exception {
//		GetObjectRequest request = new GetObjectRequest(BUCKET, "tiles/36/M/TD/2016/8/31/0/B01.jp2");
//
//		try(
//				S3Object s3Object = client.getObject(request);
//				FileOutputStream fos = new FileOutputStream("/tmp/B01.jp2");
//				S3ObjectInputStream objectInputStream = s3Object.getObjectContent()
//		) {
//			IOUtils.copy(objectInputStream, fos);
//		}

		Set<Integer> years = tileListingService.getYears(1.1, 2.2);
		System.out.println(years);

//		List<LocalDate> dates = tileListingService.squareToDate(1.1, 2.2);
//		System.out.println(dates);

//		Resource[] allFiles =  this.resourcePatternResolver.getResources("s3://"+BUCKET+"/tiles/36/M/TD/2017/1/**/*");
//		for (Resource resource : allFiles) {
//			System.out.println(resource.getFilename());
//		}
	}
}

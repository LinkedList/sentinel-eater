package cz.linkedlist;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

@SpringBootApplication
@EnableAutoConfiguration
public class SentinelEater implements CommandLineRunner {

	private static final String BUCKET = "sentinel-s2-l1c";

	@Autowired
	private AmazonS3Client client;
	@Autowired
	private ResourcePatternResolver resourcePatternResolver;

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

//		ListObjectsV2Request request1 = new ListObjectsV2Request();
//		request1.setBucketName(BUCKET);
//		request1.setPrefix("tiles/36/M/TD/2017");
//
//		ListObjectsV2Result list = client.listObjectsV2(request1);
//		for (S3ObjectSummary summary: list.getObjectSummaries()) {
//			System.out.println(" - " + summary.getKey() + "  " + "(size = " + summary.getSize() + ")");
//		}
//		System.out.println(list.isTruncated());

		Resource[] allFiles =  this.resourcePatternResolver.getResources("s3://"+BUCKET+"/tiles/36/M/TD/2017/1/**/*");
		for (Resource resource : allFiles) {
			System.out.println(resource.getFilename());
		}
	}
}

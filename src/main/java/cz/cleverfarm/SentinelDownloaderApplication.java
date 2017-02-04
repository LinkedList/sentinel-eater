package cz.cleverfarm;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileOutputStream;

@SpringBootApplication
@Configuration
public class SentinelDownloaderApplication implements CommandLineRunner {

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;
	private String bucket = "sentinel-s2-l1c";

	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}

	@Bean
	public AmazonS3Client amazonS3Client(AWSCredentials cred) {
		AmazonS3Client amazonS3Client = new AmazonS3Client(cred);
		amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
		return amazonS3Client;
	}

	@Autowired
	private AmazonS3Client client;

	public static void main(String[] args) {
		SpringApplication.run(SentinelDownloaderApplication.class, args).close();
	}

	@Override
	public void run(String... strings) throws Exception {
//		GetObjectRequest request = new GetObjectRequest(bucket, "tiles/36/M/TD/2016/8/31/0/B01.jp2");
//
//		try(
//				S3Object s3Object = client.getObject(request);
//				FileOutputStream fos = new FileOutputStream("/tmp/B01.jp2");
//				S3ObjectInputStream objectInputStream = s3Object.getObjectContent()
//		) {
//			IOUtils.copy(objectInputStream, fos);
//		}

		ListObjectsV2Request request1 = new ListObjectsV2Request();
		request1.setBucketName(bucket);
		request1.setPrefix("tiles/36/M/TD/2017");

		ListObjectsV2Result list = client.listObjectsV2(request1);
		for (S3ObjectSummary summary: list.getObjectSummaries()) {
			System.out.println(" - " + summary.getKey() + "  " + "(size = " + summary.getSize() + ")");
		}
		System.out.println(list.isTruncated());
	}
}

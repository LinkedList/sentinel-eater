package cz.linkedlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SentinelEater {

	public static final String BUCKET = "sentinel-s2-l1c";
	public static final String TILES = "tiles/";

	public static class Profiles {
		public static final String AMAZON = "amazon";
		public static final String HTTP = "http";
	}

	@Autowired
	private TileListingService listing;

	public static void main(String[] args) {
		SpringApplication.run(SentinelEater.class, args).close();
	}

}

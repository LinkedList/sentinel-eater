package cz.linkedlist

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableAutoConfiguration
open class SentinelEater {


    object Profiles {
        const val AMAZON = "amazon"
        const val HTTP = "http"
    }

    companion object {
        val BUCKET = "sentinel-s2-l1c"
        val TILES = "tiles/"
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SentinelEater::class.java, *args).close()
        }
    }

}

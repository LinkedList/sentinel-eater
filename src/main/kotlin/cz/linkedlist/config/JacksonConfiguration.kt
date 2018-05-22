package cz.linkedlist.config

import com.bedatadriven.jackson.datatype.jts.JtsModule
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@Configuration
class JacksonConfiguration {

    @Bean
    fun jtsModule(): JtsModule {
        return JtsModule()
    }

    @Bean
    fun restTemplate(objectMapper: ObjectMapper): RestTemplate {
        val rest = RestTemplate()
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = objectMapper
        //find and replace Jackson message converter with our own
        for (i in 0 until rest.messageConverters.size) {
            val httpMessageConverter = rest.messageConverters[i]
            if (httpMessageConverter is MappingJackson2HttpMessageConverter) {
                rest.messageConverters[i] = converter
            }
        }
        return rest
    }

    @Bean
    @Primary
    fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.modulesToInstall(jtsModule())
        return builder
    }

}

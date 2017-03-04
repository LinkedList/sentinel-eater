package cz.linkedlist.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        RestTemplate rest = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        //find and replace Jackson message converter with our own
        for (int i = 0; i < rest.getMessageConverters().size(); i++) {
            final HttpMessageConverter<?> httpMessageConverter = rest.getMessageConverters().get(i);
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter){
                rest.getMessageConverters().set(i, converter);
            }
        }
        return rest;
    }

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modulesToInstall(jtsModule());
        return builder;
    }

}

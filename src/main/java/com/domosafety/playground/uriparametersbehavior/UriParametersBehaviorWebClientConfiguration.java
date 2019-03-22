package com.domosafety.playground.uriparametersbehavior;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
@Import(UriParametersBehaviorJsonConfiguration.class)
public class UriParametersBehaviorWebClientConfiguration {

    @Bean
    public RestTemplate restTemplate(final ObjectMapper objectMapper) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().forEach(converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        });
        return restTemplate;
    }
}

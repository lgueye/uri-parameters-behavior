package com.domosafety.playground.uriparametersbehavior;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
@Import(UriParametersBehaviorJsonConfiguration.class)
public class UriParametersBehaviorWebClientConfiguration {

    @Bean
    public RestTemplate restTemplate(final ObjectMapper objectMapper, final ClientHttpRequestInterceptor httpLogger) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().stream() //
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter) //
                .forEach(converter -> ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper));
        restTemplate.getInterceptors().add(httpLogger);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestInterceptor loginRequestInterceptor() {
        return new ClientHttpLogger();
    }
}

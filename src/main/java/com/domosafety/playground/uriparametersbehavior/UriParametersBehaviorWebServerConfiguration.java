package com.domosafety.playground.uriparametersbehavior;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
@Import(UriParametersBehaviorJsonConfiguration.class)
public class UriParametersBehaviorWebServerConfiguration implements WebMvcConfigurer {
    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream() //
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter) //
                .forEach(converter -> ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper));
    }

}

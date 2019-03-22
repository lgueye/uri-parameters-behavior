package com.domosafety.playground.uriparametersbehavior;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
public class EventResourceTest {

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // use ISODate and not Timestamps
                .modules(new JavaTimeModule()).build();
        final EventResource underTest = new EventResource();
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    }

    @Test
    public void get_should_properly_convert_query_parameters() throws Exception {
        // Given
        final String device = UUID.randomUUID().toString();
        final String id = UUID.randomUUID().toString();
        final String phoneNumber = "+225697845";
        final String value = "foo#bar@quizz+foo-bazz//quir.";
        final Instant now = Instant.now();
        final ZonedDateTime timestamp = ZonedDateTime.ofInstant(now, ZoneId.of("+02:00"));

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", id);
        params.add("device", device);
        params.add("phoneNumber", phoneNumber);
        params.add("timestamp", timestamp.toString());
        params.add("value", value);
        final URI uri = UriComponentsBuilder.fromHttpUrl("http://foo.bar").path("/events").queryParams(params).build().toUri();

        final Event event = Event.builder().device(device).id(id).phoneNumber(phoneNumber).value(value).timestamp(timestamp).build();
        final String expectedContent = objectMapper.writeValueAsString(event);

        // When
        mockMvc.perform(get(uri)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedContent));
    }
}
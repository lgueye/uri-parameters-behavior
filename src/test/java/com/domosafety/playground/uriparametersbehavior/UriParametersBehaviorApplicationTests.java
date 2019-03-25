package com.domosafety.playground.uriparametersbehavior;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UriParametersBehaviorApplicationTests {

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate;

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Test
	public void get_should_properly_convert_query_parameters() {
		// Given
		final String device = UUID.randomUUID().toString();
		final String id = UUID.randomUUID().toString();
		final String phoneNumber = "+225697845";
		final String value = "foo#bar@quizz+foo-bazz//quir.";
		final Instant now = Instant.now();
		final ZonedDateTime timestamp = ZonedDateTime.ofInstant(now, ZoneId.of("+02:00"));
		final Map<String, String> params = new HashMap<>();
		params.put("id", id);
		params.put("device", device);
		params.put("phoneNumber", phoneNumber);
		params.put("timestamp", timestamp.toString());
		params.put("value", value);
		final MultiValueMap<String, String> paramTemplates = new LinkedMultiValueMap<>();
		paramTemplates.add("id", "{id}");
		paramTemplates.add("device", "{device}");
		paramTemplates.add("phoneNumber", "{phoneNumber}");
		paramTemplates.add("timestamp", "{timestamp}");
		paramTemplates.add("value", "{value}");

		final URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost").port(port).path("/events").queryParams(paramTemplates).encode().buildAndExpand(params).toUri();
		final Event expected = Event.builder().device(device).id(id).phoneNumber(phoneNumber).value(value).timestamp(ZonedDateTime.ofInstant(now, ZoneId.of("UTC"))).build();

		// When
		final Event actual = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Event.class).getBody();

		// Then
		assertEquals(expected, actual);
	}

}

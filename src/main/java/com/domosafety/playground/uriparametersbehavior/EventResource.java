package com.domosafety.playground.uriparametersbehavior;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
@RestController
@RequestMapping("/events")
@Slf4j
public class EventResource {

    @GetMapping
    public Event get(final Event event) {
        log.info("Received event {}", event);
        return event;
    }

}

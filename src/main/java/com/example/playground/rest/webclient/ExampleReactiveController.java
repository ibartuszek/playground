package com.example.playground.rest.webclient;

import com.example.playground.provider.ExampleWebClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/example/reactive")
public class ExampleReactiveController {

    private static final String URI_BASE = "http://localhost:8080/example/rest/resource";
    private static final String URI_WITH_VARIABLES = URI_BASE + "?date={date}&test={test}";
    private static final String DATE = "date";
    private static final String TEST = "test";
    private final ExampleWebClient client;

    public ExampleReactiveController(ExampleWebClient client) {
        this.client = client;
    }

    @RequestMapping("/resource")
    public String getResourceWithUriVariables(@RequestParam(required = false) Instant date) {
         return client.callAsyncJsonService(URI_WITH_VARIABLES, createHeaderMap(), formatIsoDateTime(date), "test:+")
                 .block();
    }

    @RequestMapping("/resource2")
    public String getResourceWithUriComponentBuilder(@RequestParam(required = false) Instant date) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URI_BASE)
                .queryParam(DATE, formatIsoDateTime(date))
                .queryParam(TEST, "test:+")
                .build()
                .toUri();
        return client.callAsyncJsonService(uri, createHeaderMap())
                .block();
    }

    @RequestMapping("/resource3")
    public String getResourceWithUriVariablesMap(@RequestParam(required = false) Instant date) {
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put(DATE, formatIsoDateTime(date));
        uriMap.put(TEST, "test:+");
        return client.callAsyncJsonService(URI_WITH_VARIABLES, createHeaderMap(), uriMap)
                .block();
    }

    private Map<String, String> createHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headerMap.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headerMap;
    }

    private String formatIsoDateTime(Instant dateTime) {
        return ZonedDateTime.ofInstant(dateTime, ZoneOffset.UTC).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME);
    }


}

package com.example.playground.rest.webclient;

import com.example.playground.provider.ExampleWebClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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
    private static final String URI_WITH_VARIABLES = URI_BASE + "?date={date}&message={message}";
    private static final String DATE = "date";
    private static final String MESSAGE = "message";
    private static final String OPERATION_BASE = "Get a hello message with the given message the date. It calls simply" +
            " asynchronously with ";
    private final ExampleWebClient client;

    public ExampleReactiveController(ExampleWebClient client) {
        this.client = client;
    }

    @Operation(summary = OPERATION_BASE + "concrete uri (uses UriComponentsBuilder).")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "E.g.: \"hello async test with concrete uri " +
                    "(2020-07-10T16:30:00Z)\"")
    })
    @GetMapping("/resource-with-concrete-uri")
    public String getResourceWithUriComponentBuilder(
            @Parameter(description = "Given date", required = true, example = "2020-07-10T16:30:00Z")
            @RequestParam Instant date) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URI_BASE)
                .queryParam(DATE, formatIsoDateTime(date))
                .queryParam(MESSAGE, "async test with concrete uri")
                .build()
                .toUri();
        return client.callAsyncJsonService(uri, createHeaderMap())
                .block();
    }

    @Operation(summary = OPERATION_BASE + "template uri and uriVariables.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "E.g.: \"hello async test with template uri and " +
                    "uriVariables (2020-07-10T16:30:00Z)\"")
    })
    @GetMapping("/resource-with-template-and-uri-variables")
    public String getResourceWithUriVariablesMap(
            @Parameter(description = "Given date", required = true, example = "2020-07-10T16:30:00Z")
            @RequestParam Instant date) {
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put(DATE, formatIsoDateTime(date));
        uriMap.put(MESSAGE, "async test with template uri and uriVariables");
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

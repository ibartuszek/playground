package com.example.playground.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import static java.time.Duration.ofMillis;

@Component
@Slf4j
public class ExampleWebClient {

    private WebClient webClient;

    public ExampleWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> callAsyncJsonService(String uri, Map<String, String> headerMap, Object... uriVariables) {
        log.info("Called uri: " + uri);
        log.info("uriVariables: " + Arrays.toString(uriVariables));
        return webClient
                .get()
                .uri(uri, uriVariables)
                .headers((headers) -> headerMap.forEach(headers::add))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(ofMillis(5000L));
    }

    public Mono<String> callAsyncJsonService(URI uri, Map<String, String> headerMap) {
        log.info("Called uri: " + uri);
        return webClient
                .get()
                .uri(uri)
                .headers((headers) -> headerMap.forEach(headers::add))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(ofMillis(5000L));
    }

    public Mono<String> callAsyncJsonService(String uri, Map<String, String> headerMap, Map<String, String> uriVariables) {
        log.info("Called uri: " + uri);
        log.info("uriVariables: " + uriVariables);
        return webClient
                .get()
                .uri(uri, uriVariables)
                .headers((headers) -> headerMap.forEach(headers::add))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(ofMillis(5000L));
    }

}

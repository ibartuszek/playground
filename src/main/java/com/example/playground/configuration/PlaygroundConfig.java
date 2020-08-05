package com.example.playground.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class PlaygroundConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);

        return webClientBuilder
                .uriBuilderFactory(defaultUriBuilderFactory)
                .build();
    }

}

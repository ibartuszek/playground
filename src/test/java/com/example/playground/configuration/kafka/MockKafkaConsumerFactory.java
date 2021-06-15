package com.example.playground.configuration.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.Map;

@RequiredArgsConstructor
public class MockKafkaConsumerFactory<S, U> implements ConsumerFactory<S, U> {

    private final MockConsumer<S, U> consumer;
    private final Map<String, Object> properties;

    @Override
    public Consumer<S, U> createConsumer(String s, String s1, String s2) {
        return consumer;
    }

    @Override
    public boolean isAutoCommit() {
        return false;
    }

    @Override
    public Map<String, Object> getConfigurationProperties() {
        return properties;
    }

}

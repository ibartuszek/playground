package com.example.playground.configuration.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MockKafkaProducerFactory implements ProducerFactory<Object, Object> {

    private final Map<String, Object> properties;
    private final ObjectMapper mapper;

    private List<MockProducer<Object, Object>> producers = new ArrayList<>();

    @Override
    public Producer<Object, Object> createProducer() {
        var producer =  new MockProducer(true, new StringSerializer(), new JsonSerializer());
        producers.add(producer);
        return producer;
    }

    public List<String> getMessageHistory(String topic) {
        var history = producers.stream()
                .map(MockProducer::history)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return history.stream()
                .filter(rec -> rec.topic().equals(topic))
                .map(rec -> {
                    try {
                        return mapper.writeValueAsString(rec.value());
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void resetProducer() {
        producers.clear();
    }

    public Map<String, Object> getConfigurationProperties() {
        return properties;
    }

}

package com.example.playground.configuration.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MockKafkaProducerConfiguration {

    @Bean
    public ProducerFactory<Object, Object> producerFactory(KafkaProperties kafkaProperties, ObjectMapper mapper) {
        Map<String, Object> config = new HashMap<>();
        kafkaProperties.getProducer().entrySet().forEach(
                entry -> config.put(entry.getKey().replace("-", "."), entry.getValue()));
        return new MockKafkaProducerFactory(config, mapper);
    }

    @Component
    @ConfigurationProperties(prefix = "kafka.test")
    @Data
    static class KafkaProperties {
        private Map<String, Object> producer;
    }

}

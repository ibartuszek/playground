package com.example.playground.configuration.kafka;

import com.example.playground.service.kafka.CustomKafkaProducer;
import com.example.playground.service.kafka.CustomMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@EnableScheduling
@Profile("!it")
public class KafkaProducerConfiguration {

    @Autowired
    private KafkaConfigurationProperties properties;

    @Bean
    public ProducerFactory<String, CustomMessage> producerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapAddress());
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    public KafkaTemplate<String, CustomMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}

package com.example.playground.configuration.kafka;

import com.example.playground.service.kafka.CustomKafkaConsumer;
import com.example.playground.service.kafka.CustomMessage;
import com.example.playground.service.kafka.CustomMessageDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@EnableKafka
public class KafkaConsumerConfiguration {

    @Autowired
    private KafkaConfigurationProperties properties;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public ConsumerFactory<String, CustomMessage> consumerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapAddress());
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        return new DefaultKafkaConsumerFactory<>(
                configMap, new StringDeserializer(), customMessageDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CustomMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public CustomKafkaConsumer customKafkaConsumer() {
        return new CustomKafkaConsumer();
    }

    @Bean
    public CustomMessageDeserializer customMessageDeserializer() {
        return new CustomMessageDeserializer(objectMapper);
    }

}

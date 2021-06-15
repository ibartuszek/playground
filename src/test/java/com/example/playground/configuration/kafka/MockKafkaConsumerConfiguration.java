package com.example.playground.configuration.kafka;

import com.example.playground.service.kafka.CustomMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MockKafkaConsumerConfiguration {

    @Autowired
    private KafkaConfigurationProperties properties;

    @Bean
    public MockConsumer<String, CustomMessage> mockConsumer() {
        return new MockConsumer<>(OffsetResetStrategy.EARLIEST);
    }

    @Bean
    public ConsumerFactory<String, CustomMessage> consumerFactory() {
        return createMockKafkaConsumerFactory(properties, mockConsumer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CustomMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setBatchListener(properties.getBatch());
        factory.setConcurrency(properties.getConcurrency());
        return factory;
    }

    private <S, U> MockKafkaConsumerFactory<S, U> createMockKafkaConsumerFactory(KafkaConfigurationProperties properties,
                                                                                 MockConsumer<S, U> mockConsumer) {
        Map<String, Object> config = createBaseConfig(properties);
        return new MockKafkaConsumerFactory<>(mockConsumer, config);
    }

    private Map<String, Object> createBaseConfig(KafkaConfigurationProperties properties) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapAddress());
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        return configMap;
    }

}

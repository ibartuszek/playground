package com.example.playground.configuration.kafka;

import com.example.playground.service.kafka.CustomKafkaConsumer;
import com.example.playground.service.kafka.CustomKafkaProducer;
import com.example.playground.service.kafka.CustomMessage;
import com.example.playground.service.kafka.KafkaMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
public class KafkaConfiguration {

    @Autowired
    private CustomKafkaProducer customKafkaProducer;

    @Autowired
    private CustomKafkaConsumer customKafkaConsumer;

    @Bean
    public KafkaMessageService kafkaMessageService() {
        BlockingDeque<CustomMessage> messageQueue = new LinkedBlockingDeque<>();
        return new KafkaMessageService(customKafkaProducer, customKafkaConsumer, messageQueue);
    }

}

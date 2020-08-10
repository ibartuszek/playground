package com.example.playground.configuration.kafka;

import com.example.playground.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@EnableScheduling
@Slf4j
public class KafkaConfiguration {

    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer();
    }

}

package com.example.playground.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class KafkaProducer {

    @Scheduled(initialDelayString = "${kafka.producer.refresh-initial-delay}", fixedDelayString = "${kafka.producer.refresh-delay}")
    public void publish() {
        log.info("TEST");
    }

}

package com.example.playground.kafka;

import com.example.playground.configuration.kafka.KafkaConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@RequiredArgsConstructor
public class CustomKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfigurationProperties properties;

    @Scheduled(initialDelayString = "${kafka.producer.refresh-initial-delay}", fixedDelayString = "${kafka.producer.refresh-delay}")
    public void sendMessage() {
        String message = "Test message";
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(properties.getTopic(), message);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

}

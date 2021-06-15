package com.example.playground.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class CustomKafkaProducer {

    private final KafkaTemplate<String, CustomMessage> kafkaTemplate;

    public void sendMessage(CustomMessage message) {
        ListenableFuture<SendResult<String, CustomMessage>> future = kafkaTemplate.send(message.getTopic(), message);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, CustomMessage> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

}

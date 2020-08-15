package com.example.playground.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;

@RequiredArgsConstructor
public class KafkaMessageService {

    private final CustomKafkaProducer producer;
    private final CustomKafkaConsumer consumer;
    private final BlockingDeque<CustomMessage> messageQueue;

    @Value("${kafka.topic}")
    private String topic;

    public String sendMessage(String message) {
        CustomMessage customMessage = CustomMessage.builder()
                .uuid(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .message(message)
                .topic(topic)
                .build();
        messageQueue.add(customMessage);
        return customMessage + " is scheduled to send kafka";
    }

    @Scheduled(initialDelayString = "${kafka.producer.refresh-initial-delay}", fixedDelayString = "${kafka.producer.refresh-delay}")
    public void sendMessages() {
        CustomMessage message = messageQueue.poll();
        if (message != null) {
            producer.sendMessage(message);
        }
    }

    public List<CustomMessage> getMessages() {
        return consumer.getMessages();
    }

}

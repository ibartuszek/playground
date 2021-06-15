package com.example.playground.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaMessageService {

    private final CustomKafkaProducer kafkaProducer;
    private final CustomKafkaConsumer kafkaConsumer;
    private final BlockingDeque<CustomMessage> messageQueue = new LinkedBlockingDeque<>();;

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
            kafkaProducer.sendMessage(message);
        }
    }

    public List<CustomMessage> getMessages() {
        return kafkaConsumer.getMessages();
    }

}

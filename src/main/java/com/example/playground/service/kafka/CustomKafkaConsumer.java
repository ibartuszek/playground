package com.example.playground.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@Slf4j
public class CustomKafkaConsumer {

    private ConcurrentMap<String, CustomMessage> messageMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(CustomMessage message) {
        log.info("Received Message: " + message);
        messageMap.putIfAbsent(message.getUuid(), message);
    }

    public List<CustomMessage> getMessages() {
        return new ArrayList<>(messageMap.values());
    }

}

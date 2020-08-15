package com.example.playground.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class CustomMessageDeserializer implements Deserializer<CustomMessage> {

    private final ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public CustomMessage deserialize(String s, byte[] bytes) {
        return deserialize(s, null, bytes);
    }

    @Override
    public CustomMessage deserialize(String topic, Headers headers, byte[] data) {
        CustomMessage customMessage = null;
        try {
            customMessage = objectMapper.readValue(data, CustomMessage.class);
        } catch (IOException e) {
            log.error("Was not able to deserialize data={}, error message={}", data, e.getMessage());
        }
        return customMessage;
    }

    @Override
    public void close() {

    }
}

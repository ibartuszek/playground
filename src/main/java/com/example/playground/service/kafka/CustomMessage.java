package com.example.playground.service.kafka;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CustomMessage {

    private final String uuid;
    private final Instant timestamp;
    private final String message;
    private final String topic;

}

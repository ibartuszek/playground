package com.example.playground.service.kafka;


import lombok.Data;

import java.time.Instant;

@Data
public class CustomMessage {

    private String uuid;
    private Instant timestamp;
    private String message;
    private String topic;

}

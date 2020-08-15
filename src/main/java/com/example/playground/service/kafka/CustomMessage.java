package com.example.playground.service.kafka;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonDeserialize(builder = CustomMessage.CustomMessageBuilder.class)
public class CustomMessage {

    private final String uuid;
    @JsonDeserialize(using = InstantDeserializer.class)
    private final Instant timestamp;
    private final String message;
    private final String topic;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CustomMessageBuilder {
        // Lombok will add constructor, setters, build method
    }

}

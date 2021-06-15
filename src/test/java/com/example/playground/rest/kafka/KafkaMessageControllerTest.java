package com.example.playground.rest.kafka;

import com.example.playground.PlaygroundApplicationTests;
import com.example.playground.service.kafka.CustomMessage;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.UUID;

public class KafkaMessageControllerTest extends PlaygroundApplicationTests {

    private static final String TOPIC = "test-topic";
    private static final String BASE_URL = "/playground/kafka";
    private static final String MESSAGES_URL = BASE_URL + "/messages";

    @Autowired
    private MockConsumer<String, CustomMessage> mockConsumer;

    @Test
    public void test() throws Exception {
        // GIVEN
        var messageId = UUID.randomUUID().toString();
        var message = CustomMessage.builder()
                .message("test")
                .timestamp(Instant.now())
                .topic(TOPIC)
                .uuid(messageId)
                .build();
        sendKafkaMessage(messageId, message);
        waitUntilConsumed(TOPIC, mockConsumer);

        // WHEN
        var actualResponse = getMvc().perform(MockMvcRequestBuilders.get(MESSAGES_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        Assertions.assertEquals(200, actualResponse.getStatus());
        Assertions.assertEquals(200, actualResponse.getStatus());
    }

    private void sendKafkaMessage(String messageId, CustomMessage message) {
        super.sendKafkaMessage(TOPIC, messageId, message, mockConsumer);
    }

}
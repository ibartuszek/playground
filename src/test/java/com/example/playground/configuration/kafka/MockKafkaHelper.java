package com.example.playground.configuration.kafka;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.playground.PlaygroundApplicationTests.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
@Slf4j
public class MockKafkaHelper {

    private static final int PARTITION = 0;
    private static final long BEGINNING_OFFSET = 0L;
    private static final long WAIT_DELAY_MILLIS = 100;
    private static final long MAX_DELAY_MILLIS = 3000;
    private static final Map<String, Long> TOPIC_OFFSETS = new HashMap<>();

    @Autowired
    protected ProducerFactory<Object, Object> kafkaProducerFactory;

    // Producer
    public void resetMessageProducers() {
        ((MockKafkaProducerFactory) kafkaProducerFactory).resetProducer();
    }

    public void assertKafkaMessageSent(String expectedBodyJson, String topic) throws JSONException {
        String value = getKafkaSingleRecordValue(topic);
        log.info("Asserting kafka topic={} message={}", topic, value);
        assertNotNull(value);
        JSONAssert.assertEquals(readJsonFile(expectedBodyJson), value, new CustomComparator(JSONCompareMode.LENIENT));
    }

    private String getKafkaSingleRecordValue(String topic) {
        List<String> messageHistory = getMessageHistory(topic);
        Assertions.assertEquals(1, messageHistory.size(), "Single kafka message was expected to produce");
        return messageHistory.iterator().next();
    }

    private List<String> getMessageHistory(String topic) {
        return  ((MockKafkaProducerFactory) kafkaProducerFactory).getMessageHistory(topic);
    }

    // Consumer
    public <S, U> void sendKafkaMessage(String topic, Object key, Object message,
                                 MockConsumer<S, U> mockConsumer) {
        if (!TOPIC_OFFSETS.containsKey(topic)) {
            HashMap<TopicPartition, Long> startOffsets = new HashMap<>();
            TopicPartition tp = new TopicPartition(topic, PARTITION);
            startOffsets.put(tp, BEGINNING_OFFSET);
            TOPIC_OFFSETS.put(topic, 0L);
            mockConsumer.updateBeginningOffsets(startOffsets);
            mockConsumer.rebalance(Collections.singleton(tp));
        }

        TOPIC_OFFSETS.compute(topic, (k, v) -> ++v);
        ConsumerRecord record = new ConsumerRecord(topic, PARTITION, TOPIC_OFFSETS.get(topic), key, message);
        mockConsumer.addRecord(record);
    }

    @SneakyThrows
    public <S, U> void waitUntilConsumed(String topic, MockConsumer<S, U> mockConsumer) {
        if (TOPIC_OFFSETS.containsKey(topic)) {
            OffsetAndMetadata offset = null;
            for (long delay = 0; hasPendingMessages(topic, offset) && delay < MAX_DELAY_MILLIS;
                 delay += WAIT_DELAY_MILLIS) {
                offset = mockConsumer.committed(new TopicPartition(topic, PARTITION));
                Thread.sleep(WAIT_DELAY_MILLIS);
            }
        }
    }

    private boolean hasPendingMessages(String topic, OffsetAndMetadata offset) {
        return offset == null || offset.offset() < TOPIC_OFFSETS.get(topic);
    }

}

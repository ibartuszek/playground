package com.example.playground;

import com.example.playground.configuration.kafka.MockKafkaHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.json.JSONException;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.util.Scanner;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlaygroundApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@Getter
@Slf4j
@ActiveProfiles("it")
@EnableMongoRepositories("com.example.playground.dal.mongo.repository")
public abstract class PlaygroundApplicationTests implements BeforeEachCallback {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockKafkaHelper mockKafkaHelper;

	@MockBean
	private Clock clock;

	protected String getObjectAsString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("object={} cannot be parsed", object);
			return null;
		}
	}

	public static String readJsonFile(String file) {
		Scanner s = new Scanner(PlaygroundApplicationTests.class.getResourceAsStream(file))
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	protected void setCurrentDate(String date) {
		when(clock.instant()).thenReturn(Instant.parse(date));
	}

	@Override
	public void beforeEach(ExtensionContext context) {
		log.info("Starting test: " + context.getTestClass() + "." + context.getTestMethod());
	}

	public void resetMessageProducers() {
		mockKafkaHelper.resetMessageProducers();
	}

	public void assertKafkaMessageSent(String expectedBodyJson, String topic) throws JSONException {
		mockKafkaHelper.assertKafkaMessageSent(expectedBodyJson, topic);
	}

	public <S, U> void sendKafkaMessage(String topic, Object key, Object message, MockConsumer<S, U> mockConsumer) {
		mockKafkaHelper.sendKafkaMessage(topic, key, message, mockConsumer);
	}

	public <S, U> void waitUntilConsumed(String topic, MockConsumer<S, U> mockConsumer) {
		mockKafkaHelper.waitUntilConsumed(topic, mockConsumer);
	}

}

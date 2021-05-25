package com.example.playground.mongo;

import com.example.playground.PlaygroundApplicationTests;
import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Getter
public class AbstractMongoTest extends PlaygroundApplicationTests {

    protected static final String COLLECTION = "exampleCollection";

    protected static final String BASE_URL = "/playground/mongo/example-entry/";
    protected static final String MESSAGE = "message";
    protected static final String MESSAGE_2 = "message 2";
    protected static final String ID = "1";
    protected static final String ID_2 = "2";
    protected static final String MONGO_ID = "_id";
    protected static final Instant TIME_STAMP = Instant.parse("2020-10-14T08:00:00Z");
    protected static final Instant TIME_STAMP_2 = Instant.parse("2020-10-14T09:00:00Z");
    protected static final Date DATE = Date.from(TIME_STAMP);
    protected static final Date DATE_2 = Date.from(TIME_STAMP_2);


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ExampleRepository exampleRepository;

    @BeforeEach
    public void setUp() {
        exampleRepository.save(createExampleEntity(ID, MESSAGE, TIME_STAMP, DATE));
    }

    @AfterEach
    public void cleanCollection() {
        List<String> idList = mongoTemplate.findAll(ExampleEntry.class).stream()
                .map(ExampleEntry::getId)
                .collect(Collectors.toList());
        Query toDeleteAll = new Query(where(MONGO_ID).in(idList));
        mongoTemplate.findAllAndRemove(toDeleteAll, COLLECTION);
        assertEquals(0, mongoTemplate.findAll(ExampleEntry.class).size());
    }

    @Test
    public void testSetUp() {
        // GIVEN

        // WHEN
        var actual = mongoTemplate.findAll(ExampleEntry.class, COLLECTION);
        // THEN
        assertEquals(1, actual.size());
        assertEquals(ID, actual.get(0).getId());
        assertEquals(MESSAGE, actual.get(0).getMessage());
    }

    protected Document createDocument(String id, String message, Instant timeStamp, Date date) {
        return createDocument(id, message, timeStamp.toString(), date);
    }

    private Document createDocument(String id, String message, String timeStamp, Date date) {
        Document document = new Document();
        document.put("_id", id);
        document.put("message", message);
        document.put("timeStamp", timeStamp);
        document.put("date", date);
        return document;
    }

    protected ExampleEntry createExampleEntity(String id, String message, Instant timeStamp, Date date) {
        return ExampleEntry.builder()
                .id(id)
                .message(message)
                .timeStamp(timeStamp)
                .date(date)
                .build();
    }

    protected void testSetupWithJson() {
        String fileContent = readJsonFile("/mongo/init-db.json");
        try {
            ExampleEntry[] result = getObjectMapper().readValue(fileContent, ExampleEntry[].class);
            Arrays.stream(result).forEach(entry -> exampleRepository.save(entry));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

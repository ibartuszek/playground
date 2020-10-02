package com.example.playground.mongo;

import com.example.playground.PlaygroundApplicationTests;
import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
public class ExampleEntryControllerTest extends PlaygroundApplicationTests {

    private static final String COLLECTION = "exampleCollection";

    private static final String BASE_URL = "/playground/mongo/example-entry/";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_2 = "message 2";
    private static final String ID = "1";
    private static final String ID_2 = "2";
    public static final String MONGO_ID = "_id";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ExampleRepository exampleRepository;

    @BeforeEach
    public void setUp() {
        mongoTemplate.save(createDocument(ID, MESSAGE), COLLECTION);
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

    @Test
    public void testRepository() {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE);

        // WHEN
        Optional<ExampleEntry> actual = exampleRepository.findById(ID);
        // THEN
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testGetById() throws Exception {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE);

        // WHEN
        var actual = getMvc().perform(MockMvcRequestBuilders.get(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        var actualEntry = getObjectMapper().readValue(actual.getContentAsString(), ExampleEntry.class);
        assertEquals(expected, actualEntry);
    }

    @Test
    public void testGetAll() throws Exception {
        // GIVEN
        var expected1 = createExampleEntity(ID, MESSAGE);
        var expected2 = createExampleEntity(ID_2, MESSAGE_2);
        mongoTemplate.save(createDocument(ID_2, MESSAGE_2), COLLECTION);

        // WHEN
        var actual = getMvc().perform(MockMvcRequestBuilders.get(BASE_URL + "all")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        var actualEntry = Arrays.asList(getObjectMapper().readValue(actual.getContentAsString(), ExampleEntry[].class));
        assertTrue(actualEntry.contains(expected1));
        assertTrue(actualEntry.contains(expected2));
    }

    @Test
    public void testCreate() throws Exception {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID_2, MESSAGE_2);

        // WHEN
        ExampleEntry entryToCreate = createExampleEntity(null, MESSAGE_2);
        var actual = getMvc().perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(getObjectAsString(entryToCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        var actualEntry = getObjectMapper().readValue(actual.getContentAsString(), ExampleEntry.class);
        assertEquals(expected, actualEntry);
        log.info("Created entry`s id:{}", actualEntry.getId());
        assertEquals(2, mongoTemplate.findAll(ExampleEntry.class).size());
    }

    @Test
    public void testUpdate() throws Exception {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE_2);

        // WHEN
        ExampleEntry entryToUpdate = createExampleEntity(ID, MESSAGE_2);
        var actual = getMvc().perform(MockMvcRequestBuilders.put(BASE_URL)
                .content(getObjectAsString(entryToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        var actualEntry = getObjectMapper().readValue(actual.getContentAsString(), ExampleEntry.class);
        assertEquals(expected, actualEntry);
        var exampleEntryList = mongoTemplate.findAll(ExampleEntry.class);
        assertEquals(1, exampleEntryList.size());
        assertEquals(MESSAGE_2, exampleEntryList.get(0).getMessage());
    }

    @Test
    public void testDelete() throws Exception {
        // GIVEN

        // WHEN
        var actual = getMvc().perform(MockMvcRequestBuilders.delete(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        assertEquals(0, mongoTemplate.findAll(ExampleEntry.class).size());
    }

    @Test
    public void initDbTest() {
        // WHEN
        testSetupWithJson();

        // THEN
        assertEquals(4, mongoTemplate.findAll(ExampleEntry.class).size());
    }

    private Document createDocument(String id, String message) {
        Document document = new Document();
        document.put("_id", id);
        document.put("message", message);
        return document;
    }

    private ExampleEntry createExampleEntity(String id, String message) {
        return ExampleEntry.builder()
                .id(id)
                .message(message)
                .build();
    }

    private void testSetupWithJson() {
        String fileContent = readJsonFile("/mongo/init-db.json");
        try {
            Map<String,Object>[] result = getObjectMapper().readValue(fileContent, HashMap[].class);
            Arrays.stream(result).forEach(bsonObject -> mongoTemplate.save(createDocument(bsonObject), COLLECTION));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(Map<String, Object> bsonObject) {
        return createDocument((String) bsonObject.get(MONGO_ID), (String) bsonObject.get("message"));
    }

}

package com.example.playground.mongo;

import com.example.playground.PlaygroundApplicationTests;
import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ExampleEntryControllerTest extends PlaygroundApplicationTests {

    private static final String BASE_URL = "/playground/mongo/example-entry/";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_2 = "message 2";
    private static final String ID = "1";
    private static final String ID_2 = "2";

    @Autowired
    private MongoCollection<Document> mongoCollection;

    @Autowired
    private ExampleRepository exampleRepository;

    @BeforeEach
    public void setUp() {
        mongoCollection.insertOne(createDocument(ID, MESSAGE));
    }

    @AfterEach
    public void cleanCollection() {
        BasicDBObject document = new BasicDBObject();
        mongoCollection.deleteMany(document);
        assertEquals(0, mongoCollection.countDocuments());
    }

    @Test
    public void testSetUp() {
        // GIVEN

        // WHEN

        // THEN
        assertEquals(1, mongoCollection.countDocuments());
        assertEquals(ID, mongoCollection.find().first().get("_id"));
        assertEquals(MESSAGE, mongoCollection.find().first().get("message"));
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
        mongoCollection.insertOne(createDocument(ID_2, MESSAGE_2));

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
        assertEquals(2, mongoCollection.countDocuments());
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
        assertEquals(1, mongoCollection.countDocuments());
        assertEquals(MESSAGE_2, mongoCollection.find().first().get("message"));
    }

    @Test
    public void testDelete() throws Exception {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE);

        // WHEN
        var actual = getMvc().perform(MockMvcRequestBuilders.delete(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        assertEquals(0, mongoCollection.countDocuments());
    }

    @Test
    public void initDbTest() {
        // WHEN
        testSetupWithJson();

        // THEN
        assertEquals(4, mongoCollection.countDocuments());
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
            List<Document> documentList = new ArrayList<>();
            Map<String,Object>[] result = getObjectMapper().readValue(fileContent, HashMap[].class);
            Arrays.stream(result).forEach(bsonObject -> documentList.add(createDocument(bsonObject)));
            mongoCollection.insertMany(documentList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(Map<String, Object> bsonObject) {
        return createDocument((String) bsonObject.get("_id"), (String) bsonObject.get("message"));
    }

}

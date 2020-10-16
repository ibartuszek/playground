package com.example.playground.mongo;

import com.example.playground.dal.mongo.ExampleEntry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ExampleEntryControllerTest extends AbstractMongoTest {

    @Test
    public void testGetById() throws Exception {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE, TIME_STAMP, DATE);

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
        var expected1 = createExampleEntity(ID, MESSAGE, TIME_STAMP, DATE);
        var expected2 = createExampleEntity(ID_2, MESSAGE_2, TIME_STAMP_2, DATE_2);
        getMongoTemplate().save(createDocument(ID_2, MESSAGE_2, TIME_STAMP_2, DATE_2), COLLECTION);

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
        ExampleEntry expected = createExampleEntity(ID_2, MESSAGE_2, TIME_STAMP_2, DATE_2);

        // WHEN
        ExampleEntry entryToCreate = createExampleEntity(null, MESSAGE_2, TIME_STAMP_2, DATE_2);
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
        assertEquals(2, getMongoTemplate().findAll(ExampleEntry.class).size());
    }

    @Test
    public void testUpdate() throws Exception {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE_2, TIME_STAMP_2, DATE_2);

        // WHEN
        ExampleEntry entryToUpdate = createExampleEntity(ID, MESSAGE_2, TIME_STAMP_2, DATE_2);
        var actual = getMvc().perform(MockMvcRequestBuilders.put(BASE_URL)
                .content(getObjectAsString(entryToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        var actualEntry = getObjectMapper().readValue(actual.getContentAsString(), ExampleEntry.class);
        assertEquals(expected, actualEntry);
        var exampleEntryList = getMongoTemplate().findAll(ExampleEntry.class);
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
        assertEquals(0, getMongoTemplate().findAll(ExampleEntry.class).size());
    }

}

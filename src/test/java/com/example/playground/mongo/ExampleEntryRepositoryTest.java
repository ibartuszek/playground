package com.example.playground.mongo;

import com.example.playground.dal.mongo.ExampleEntry;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleEntryRepositoryTest extends AbstractMongoTest {

    @Test
    public void testRepository() {
        // GIVEN
        ExampleEntry expected = createExampleEntity(ID, MESSAGE, TIME_STAMP, DATE);

        // WHEN
        Optional<ExampleEntry> actual = getExampleRepository().findById(ID);
        // THEN
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void initDbTest() {
        // WHEN
        testSetupWithJson();

        // THEN
        assertEquals(4, getMongoTemplate().findAll(ExampleEntry.class).size());
    }

    @Test
    public void testCustomQueryWithInstant() {
        // GIVEN
        String currentTime = "2020-10-14T14:00:00.000Z";
        setCurrentDate(currentTime);
        testSetupWithJson();
        var expected = getExampleRepository().findAll().stream()
                .filter(entry -> entry.getTimeStamp().isBefore(Instant.parse(currentTime)))
                .collect(Collectors.toList());

        // WHEN
        var actual = getExampleRepository().findAllByTimeStampBefore(Instant.parse(currentTime));

        // THEN
        assertEquals(expected, actual);
    }

    @Test
    public void testCustomQueryWithDate() {
        // GIVEN
        String currentTime = "2020-10-14T14:00:00.000Z";
        setCurrentDate(currentTime);
        testSetupWithJson();
        Instant currentInstant = Instant.parse(currentTime);
        var expected = getExampleRepository().findAll().stream()
                .filter(entry -> entry.getTimeStamp().isBefore(currentInstant))
                .collect(Collectors.toList());

        // WHEN
        var actual = getExampleRepository().findAllByDateBefore(Date.from(currentInstant));

        // THEN
        assertEquals(expected, actual);
    }

}

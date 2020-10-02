package com.example.playground.configuration.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
@Primary
@Slf4j
@Getter
// Enable Spring autoConfiguration -> do not extend AbstractMongoClientConfiguration
// It is used by ExampleEntryControllerTes it tests
public class MongoConfiguration {

    private static final String COLLECTION_NAME = "exampleCollection";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoTemplate.getDb();
    }

    @Bean
    public MongoCollection<Document> mongoCollection() {
        return mongoDatabase().getCollection(COLLECTION_NAME);
    }

}

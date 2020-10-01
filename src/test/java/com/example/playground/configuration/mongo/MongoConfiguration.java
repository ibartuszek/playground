package com.example.playground.configuration.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@Primary
@RequiredArgsConstructor
@Slf4j
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private static final String COLLECTION_NAME = "exampleCollection";

    private final MongoConfigurationProperties properties;

    @Bean
    public MongoServer mongoServer() {
        log.info("Creating mongoServer");
        return new MongoServer(new MemoryBackend());
    }

    @Bean
    public MongoCollection<Document> collection() {
        log.info("Creating mongoCollection");
        return mongoClient()
                .getDatabase(properties.getDatabaseName())
                .getCollection(COLLECTION_NAME);
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        log.info("Creating mongo client");
        String connectionString = "mongodb:/" + mongoServer().bind() + "/" + properties.getDatabaseName();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

}

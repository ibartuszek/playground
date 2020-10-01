package com.example.playground.configuration.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.text.MessageFormat;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.playground.dal.mongo.repository")
@ConditionalOnProperty(value = "mongo.enabled", havingValue = "true")
@RequiredArgsConstructor
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private final MongoConfigurationProperties properties;

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }

    @Override
    public MongoClient mongoClient() {
        String connectionString = MessageFormat.format("mongodb://{0}:{1}/{2}",
                properties.getAddress(), properties.getPort() + "", properties.getDatabaseName());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

}

package com.example.playground.configuration.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.text.MessageFormat;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.playground.dal.mongo.repository")
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    // TODO
    private static final String DB_ADDRESS = "192.168.175.107";
    private static final String DB_PORT = "27017";
    private static final String DB_NAME = "playground";
    private static final String MONGO_CONNECTION_STRING = MessageFormat.format("mongodb://{0}:{1}/{2}",
            DB_ADDRESS, DB_PORT, DB_NAME);

    @Override
    protected String getDatabaseName() {
        return DB_NAME;
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(MONGO_CONNECTION_STRING);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

}

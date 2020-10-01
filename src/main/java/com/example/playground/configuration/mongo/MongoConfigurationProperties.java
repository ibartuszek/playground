package com.example.playground.configuration.mongo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mongo")
@Data
public class MongoConfigurationProperties {

    private boolean enabled;
    private String address;
    private int port;
    private String databaseName;

}

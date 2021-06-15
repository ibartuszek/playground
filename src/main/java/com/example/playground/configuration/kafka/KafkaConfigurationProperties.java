package com.example.playground.configuration.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfigurationProperties {

    private boolean enabled;
    private String bootstrapAddress;
    private String topic;
    private String groupId;
    private Boolean batch;
    private Integer concurrency;

}

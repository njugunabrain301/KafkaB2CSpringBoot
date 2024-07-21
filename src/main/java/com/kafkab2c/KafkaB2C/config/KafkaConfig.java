package com.kafkab2c.KafkaB2C.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * KafkaConfig class defines Kafka topic configurations.
 */
@Configuration
public class KafkaConfig {

    /**
     * Creates a new Kafka topic named "B2CRequest".
     *
     * @return a NewTopic object representing the "B2CRequest" topic.
     */
    public NewTopic B2CRequestTopic() {
        return TopicBuilder.name("B2CRequest").build();
    }

    /**
     * Creates a new Kafka topic named "B2CPending".
     *
     * @return a NewTopic object representing the "B2CPending" topic.
     */
    public NewTopic B2CPendingTopic() {
        return TopicBuilder.name("B2CPending").build();
    }
}

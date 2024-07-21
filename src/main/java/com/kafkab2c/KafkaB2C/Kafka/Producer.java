package com.kafkab2c.KafkaB2C.Kafka;

import com.kafkab2c.KafkaB2C.Models.B2CRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Producer service for sending B2CRequest messages to Kafka topics.
 */
@Service
public class Producer {

    private final KafkaTemplate<String, B2CRequest> kafkaTemplate;

    /**
     * Constructor for initializing the Producer service.
     *
     * @param kafkaTemplate The KafkaTemplate instance used for sending messages.
     */
    public Producer(KafkaTemplate<String, B2CRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a B2CRequest message to the "B2CRequest" Kafka topic.
     *
     * @param request The B2CRequest message to be sent.
     * @return A boolean indicating the success of the operation.
     */
    public boolean sendB2CRequest(B2CRequest request) {
        boolean success = true;
        // Create a Kafka message with the payload and topic header
        Message<B2CRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "B2CRequest")
                .build();
        // Send the message to the Kafka topic
        kafkaTemplate.send(message);
        return success;
    }

    /**
     * Sends a B2CRequest message to the "B2CPending" Kafka topic.
     *
     * @param request The B2CRequest message to be sent.
     * @return A boolean indicating the success of the operation.
     */
    public boolean B2CRequestSent(B2CRequest request) {
        boolean success = true;
        // Create a Kafka message with the payload and topic header
        Message<B2CRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "B2CPending")
                .build();
        // Send the message to the Kafka topic
        kafkaTemplate.send(message);
        return success;
    }
}

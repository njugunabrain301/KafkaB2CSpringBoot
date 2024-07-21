package com.kafkab2c.KafkaB2C.Database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a record of a request in the MongoDB database.
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRecord {

    /** The unique identifier of the record. */
    @Id
    private String id;

    /** The remarks associated with the request. */
    private String remarks;

    /** The amount involved in the request. */
    private int amount;

    /** The occasion for which the request is made. */
    private String occassion;

    /** The phone number associated with the request. */
    private String phone;

    /** The status of the request. */
    private String status;

    /** The conversation ID associated with the request. */
    private String conversationID;

    /** The date and time when the record was created. */
    private String dateCreated;

    /** The date and time when the record was accepted. */
    private String dateAccepted;

    /** The number of attempts made to process the request. */
    private int attemptCount;
}

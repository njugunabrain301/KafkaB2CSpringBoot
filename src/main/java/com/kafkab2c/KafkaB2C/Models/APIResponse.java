package com.kafkab2c.KafkaB2C.Models;

import com.kafkab2c.KafkaB2C.Database.RequestRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * APIResponse class represents the response structure for API operations.
 * It includes a message and optionally a list of records.
 */
@Data
public class APIResponse {

    /**
     * Message providing status or information about the API response.
     */
    private String message;

    /**
     * List of RequestRecord objects included in the response.
     * Initialized to an empty list if not provided.
     */
    private List<RequestRecord> records;

    /**
     * Constructor to initialize APIResponse with a message and a list of records.
     *
     * @param message The response message.
     * @param records The list of RequestRecord objects to include in the response.
     */
    public APIResponse(String message, List<RequestRecord> records) {
        this.message = message;
        this.records = records;
    }

    /**
     * Constructor to initialize APIResponse with only a message.
     * Initializes the records list to an empty ArrayList.
     *
     * @param message The response message.
     */
    public APIResponse(String message) {
        this.message = message;
        records = new ArrayList<>();
    }
}

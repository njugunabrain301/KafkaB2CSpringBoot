package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * APIRequest class represents a request payload for the API.
 * It includes fields for payment details and provides
 * a default constructor and a custom toString method.
 */
@Data
public class APIRequest {

    /**
     * The amount of the transaction.
     */
    private int amount;

    /**
     * The phone number of the recipient, starting with country code.
     */
    private String partyB;

    /**
     * Remarks or description of the transaction.
     */
    private String remarks;

    /**
     * The occasion or purpose of the transaction.
     */
    private String occasion;

    /**
     * Default constructor for APIRequest.
     */
    public APIRequest() {
    }

    /**
     * Returns a string representation of the APIRequest object.
     *
     * @return A string with the format:
     * "APIRequest{amount=..., partyB='...', remarks='...', occasion='...'}"
     */
    @Override
    public String toString() {
        return "APIRequest{" +
                "amount=" + amount +
                ", partyB='" + partyB + '\'' +
                ", remarks='" + remarks + '\'' +
                ", occasion='" + occasion + '\'' +
                '}';
    }
}

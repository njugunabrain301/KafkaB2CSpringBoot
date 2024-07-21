package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * ResultObject class represents the result details of a B2C transaction.
 */
@Data
public class ResultObject {

    /** Unique identifier for the conversation. */
    private String ConversationID;

    /** Contains additional reference data associated with the transaction. */
    private ReferenceDataObject ReferenceData;

    /** Unique identifier for the originator's conversation. */
    private String OriginatorConversationID;

    /** Description of the result of the transaction. */
    private String ResultDesc;

    /** Type of result (e.g., success, failure). */
    private int ResultType;

    /** Code representing the result status of the transaction. */
    private int ResultCode;

    /** Parameters related to the result. */
    private ResultParametersObject ResultParameters;

    /** Unique identifier for the transaction. */
    private String TransactionID;
}

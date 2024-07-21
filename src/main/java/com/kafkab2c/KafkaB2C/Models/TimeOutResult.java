package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * TimeOutResult class represents the result of a timeout response in a transaction.
 */
@Data
public class TimeOutResult {

    /** The type of result, indicating the nature of the timeout. */
    private int ResultType;

    /** A code representing the result of the timeout. */
    private int ResultCode;

    /** A description of the result, providing details about the timeout. */
    private String ResultDesc;

    /** An identifier for the originator of the conversation. */
    private String OriginatorConversationID;

    /** An identifier for the conversation associated with the timeout. */
    private String ConversationID;

    /** An identifier for the transaction that experienced a timeout. */
    private String TransactionID;
}

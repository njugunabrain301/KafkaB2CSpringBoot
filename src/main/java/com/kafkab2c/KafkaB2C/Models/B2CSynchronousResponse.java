package com.kafkab2c.KafkaB2C.Models;

import com.kafkab2c.KafkaB2C.utils.JSONUtils;
import lombok.Data;

import java.io.IOException;
import java.util.Map;

/**
 * B2CSynchronousResponse class represents the response received from a Business to Customer (B2C) transaction.
 */
@Data
public class B2CSynchronousResponse {

    /** Unique identifier for the conversation. */
    private String ConversationID;

    /** Unique identifier for the originator's conversation. */
    private String OriginatorConversationID;

    /** Code indicating the result of the response. */
    private String ResponseCode;

    /** Description of the response. */
    private String ResponseDescription;

    /**
     * Constructs a B2CSynchronousResponse from a JSON response string.
     *
     * @param response The JSON string containing response details.
     */
    public B2CSynchronousResponse(String response) {
        try {
            Map<String, Object> values = JSONUtils.jsonToMap(response);
            this.ConversationID = (String) values.get("ConversationID");
            this.ResponseCode = (String) values.get("ResponseCode");
            this.OriginatorConversationID = (String) values.get("OriginatorConversationID");
            this.ResponseDescription = (String) values.get("ResponseDescription");
        } catch (IOException e) {
            // Initialize fields to empty strings in case of an error
            this.ConversationID = "";
            this.ResponseCode = "";
            this.OriginatorConversationID = "";
            this.ResponseDescription = "";
            e.printStackTrace();
        }
    }

    /**
     * Default constructor initializing fields to empty strings.
     */
    public B2CSynchronousResponse() {
        this.ConversationID = "";
        this.ResponseCode = "";
        this.OriginatorConversationID = "";
        this.ResponseDescription = "";
    }

    @Override
    public String toString() {
        return "B2CSynchronousResponse{" +
                "ConversationID='" + ConversationID + '\'' +
                ", OriginatorConversationID='" + OriginatorConversationID + '\'' +
                ", ResponseCode='" + ResponseCode + '\'' +
                ", ResponseDescription='" + ResponseDescription + '\'' +
                '}';
    }
}

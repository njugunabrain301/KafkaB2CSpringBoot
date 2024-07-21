package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * B2CRequest class represents a request for a Business to Customer (B2C) transaction.
 * It includes all necessary details and methods to handle the request.
 */
@Data
public class B2CRequest {

    /** Remarks associated with the B2C request. */
    private String Remarks;

    /** Amount for the B2C transaction. */
    private int Amount;

    /** Name of the initiator of the B2C transaction. */
    private String InitiatorName;

    /** Security credential for authentication. */
    private String SecurityCredential;

    /** Occasion for the B2C transaction. */
    private String Occassion;

    /** Command ID for the type of transaction. */
    private String CommandID;

    /** Response from the B2C synchronous operation. */
    private B2CSynchronousResponse b2CSynchronousResponse;

    /** Shortcode of the party initiating the transaction. */
    private String PartyA;

    /** Phone number of the recipient (Party B). */
    private String PartyB;

    /** URL to receive the result of the B2C transaction. */
    private String ResultURL;

    /** URL to receive the timeout response of the B2C transaction. */
    private String QueueTimeOutURL;

    /**
     * Constructs a B2CRequest from an APIRequest.
     *
     * @param request The APIRequest object containing transaction details.
     */
    public B2CRequest(APIRequest request) {
        setAmount(request.getAmount());
        setCommandID("BusinessPayment");
        setPartyB(request.getPartyB());
        setSecurityCredential("");
        setRemarks(request.getRemarks());
        setOccassion(request.getOccasion());
    }

    /**
     * Default constructor initializing fields with default values.
     */
    public B2CRequest() {
        setAmount(0);
        setCommandID("BusinessPayment");
        setPartyB("");
        setSecurityCredential("");
        setRemarks("");
        setOccassion("");
    }

    /**
     * Sets the B2CSynchronousResponse for this request.
     *
     * @param b2CSynchronousResponse The synchronous response to set.
     */
    public void setB2CSynchronousResponse(B2CSynchronousResponse b2CSynchronousResponse) {
        this.b2CSynchronousResponse = b2CSynchronousResponse;
    }

    /**
     * Generates a map representation of the B2C request payload.
     *
     * @return A map containing the request payload.
     */
    public Map<String, Object> generateHashMap() {
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("InitiatorName", getInitiatorName());
        requestPayload.put("CommandID", getCommandID());
        requestPayload.put("Amount", getAmount());
        requestPayload.put("PartyA", getPartyA());
        requestPayload.put("PartyB", getPartyB());
        requestPayload.put("Remarks", getRemarks());
        requestPayload.put("QueueTimeOutURL", getQueueTimeOutURL());
        requestPayload.put("ResultURL", getResultURL());
        requestPayload.put("Occasion", getOccassion());
        return requestPayload;
    }

    @Override
    public String toString() {
        return "B2CRequest{" +
                "QueueTimeOutURL='" + QueueTimeOutURL + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Amount=" + Amount +
                ", InitiatorName='" + InitiatorName + '\'' +
                ", SecurityCredential='" + SecurityCredential + '\'' +
                ", Occassion='" + Occassion + '\'' +
                ", CommandID='" + CommandID + '\'' +
                ", PartyA='" + PartyA + '\'' +
                ", PartyB='" + PartyB + '\'' +
                ", ResultURL='" + ResultURL + '\'' +
                '}';
    }
}

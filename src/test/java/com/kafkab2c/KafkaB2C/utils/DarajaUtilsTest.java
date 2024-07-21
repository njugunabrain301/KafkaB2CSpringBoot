package com.kafkab2c.KafkaB2C.utils;

import com.kafkab2c.KafkaB2C.Models.B2CRequest;
import com.kafkab2c.KafkaB2C.Models.B2CSynchronousResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DarajaUtilsTest is a test class for the DarajaUtils utility class.
 * It contains test methods to verify the functionality of various utility methods in DarajaUtils.
 */
class DarajaUtilsTest {

    /**
     * Tests the getSecurityCredentials method in DarajaUtils.
     * Verifies that the method returns a non-null security credentials string.
     */
    @Test
    public void getSecurityCredentials() {
        String securityCredentials = DarajaUtils.getSecurityCredentials();
        assertNotNull(securityCredentials);
    }

    /**
     * Tests the getAccessToken method in DarajaUtils.
     * Verifies that the method returns a non-null access token string.
     */
    @Test
    public void getAccessToken() {
        String token = DarajaUtils.getAccessToken();
        assertNotNull(token);
    }

    /**
     * Tests the sendB2CRequest method in DarajaUtils.
     * Verifies that the method sends a B2C request and returns a non-null response
     * with valid response code and conversation ID.
     */
    @Test
    public void sendB2CRequest() {
        // Create a sample B2CRequest
        B2CRequest request = new B2CRequest();
        request.setPartyB("254717563148");
        request.setAmount(20);
        request.setRemarks("Test");
        request.setOccassion("Test");
        request.setInitiatorName(DarajaUtils.initiatorName);
        request.setPartyA(DarajaUtils.shortCode);
        request.setResultURL(DarajaUtils.resultURL);
        request.setQueueTimeOutURL(DarajaUtils.queueTimeOutURL);

        // Get access token
        String accessToken = DarajaUtils.getAccessToken();

        // Send B2C request
        B2CSynchronousResponse response = DarajaUtils.sendB2CRequest(accessToken, request);

        // Assert response is not null and contains required fields
        assertNotNull(response);
        assertNotNull(response.getResponseCode());
        assertNotNull(response.getConversationID());
    }

    /**
     * Tests the checkTransactionStatus method in DarajaUtils.
     * Verifies that the method checks the transaction status and returns a non-null result
     * with a response code of "0".
     */
    @Test
    public void checkTransactionStatus() {
        // Create a sample B2CRequest
        B2CRequest request = new B2CRequest();
        request.setPartyB("254717563148");
        request.setAmount(20);
        request.setRemarks("Test");
        request.setOccassion("Test");
        request.setInitiatorName(DarajaUtils.initiatorName);
        request.setPartyA(DarajaUtils.shortCode);
        request.setResultURL(DarajaUtils.resultURL);
        request.setQueueTimeOutURL(DarajaUtils.queueTimeOutURL);

        // Get access token
        String accessToken = DarajaUtils.getAccessToken();

        // Send B2C request
        B2CSynchronousResponse response = DarajaUtils.sendB2CRequest(accessToken, request);
        assertNotNull(response);
        assertNotNull(response.getResponseCode());
        assertNotNull(response.getConversationID());

        // Check transaction status
        B2CSynchronousResponse statusResult = DarajaUtils.checkTransactionStatus(response.getConversationID());
        assertNotNull(statusResult);
        assertEquals("0", statusResult.getResponseCode());
    }
}

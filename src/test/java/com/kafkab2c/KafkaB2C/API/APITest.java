package com.kafkab2c.KafkaB2C.API;

import com.kafkab2c.KafkaB2C.Database.RequestRecord;
import com.kafkab2c.KafkaB2C.Database.RequestRecordRepo;
import com.kafkab2c.KafkaB2C.Kafka.Producer;
import com.kafkab2c.KafkaB2C.Models.*;
import com.kafkab2c.KafkaB2C.utils.DarajaUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * APITest is a test class for the API controller.
 * It verifies the functionality of various API endpoints using MockMvc for HTTP requests and responses.
 */
@WebMvcTest(API.class)
public class APITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestRecordRepo recordRepo;

    @MockBean
    private Producer producer;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the GET /api/records endpoint.
     * Verifies that the endpoint returns a list of request records in JSON format.
     */
    @Test
    void getRecords() throws Exception {
        List<RequestRecord> mockRecords = List.of(
                new RequestRecord("1", "remarks1", 100, "occasion1", "1234567890", "status1", "conv1", "2023-07-01T10:00:00", "2023-07-01T12:00:00", 1),
                new RequestRecord("2", "remarks2", 200, "occasion2", "0987654321", "status2", "conv2", "2023-07-02T11:00:00", "2023-07-02T13:00:00", 2)
        );
        given(recordRepo.findAll()).willReturn(mockRecords);

        String expectedResponse = """
        {
            "message": "Success",
            "records": [
                {
                    "id": "1",
                    "remarks": "remarks1",
                    "amount": 100,
                    "occassion": "occasion1",
                    "phone": "1234567890",
                    "status": "status1",
                    "conversationID": "conv1",
                    "dateCreated": "2023-07-01T10:00:00",
                    "dateAccepted": "2023-07-01T12:00:00",
                    "attemptCount": 1
                },
                {
                    "id": "2",
                    "remarks": "remarks2",
                    "amount": 200,
                    "occassion": "occasion2",
                    "phone": "0987654321",
                    "status": "status2",
                    "conversationID": "conv2",
                    "dateCreated": "2023-07-02T11:00:00",
                    "dateAccepted": "2023-07-02T13:00:00",
                    "attemptCount": 2
                }
            ]
        }
        """;

        mockMvc.perform(get("/api/records"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    /**
     * Tests the POST /api/timeout endpoint.
     * Verifies that the endpoint correctly processes timeout notifications and updates the request record status.
     */
    @Test
    void timeout() throws Exception {
        RequestRecord existingRecord = new RequestRecord("1", "remarks1", 100, "occasion1", "1234567890", "PENDING", "conv1", "2023-07-01T10:00:00", null, 1);
        List<RequestRecord> mockRecords = List.of(existingRecord);
        given(recordRepo.findAll()).willReturn(mockRecords);

        TimeOutResult timeOutResult = new TimeOutResult();
        timeOutResult.setResultType(1);
        timeOutResult.setResultCode(0); // success
        timeOutResult.setResultDesc("Transaction successful");
        timeOutResult.setOriginatorConversationID("origin1");
        timeOutResult.setConversationID("conv1");
        timeOutResult.setTransactionID("trans1");

        B2CTimeOutResponse timeoutResponse = new B2CTimeOutResponse();
        timeoutResponse.setResult(timeOutResult);

        mockMvc.perform(post("/api/timeout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeoutResponse)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "message": "Received"
                }
                """));

        ArgumentCaptor<RequestRecord> captor = ArgumentCaptor.forClass(RequestRecord.class);
        verify(recordRepo).save(captor.capture());

        RequestRecord savedRecord = captor.getValue();
        assertNotNull(savedRecord.getDateAccepted());
        assertEquals("SUCCESSFUL", savedRecord.getStatus());
        assertEquals("conv1", savedRecord.getConversationID());
    }

    /**
     * Tests the POST /api/callback endpoint.
     * Verifies that the endpoint correctly processes callback notifications and updates the request record status.
     */
    @Test
    void callback() throws Exception {
        RequestRecord existingRecord = new RequestRecord("1", "remarks1", 100, "occasion1", "1234567890", "PENDING", "conv1", "2023-07-01T10:00:00", null, 1);
        List<RequestRecord> mockRecords = List.of(existingRecord);
        given(recordRepo.findAll()).willReturn(mockRecords);

        ResultObject resultObject = new ResultObject();
        resultObject.setConversationID("conv1");
        resultObject.setResultCode(0); // success
        resultObject.setResultDesc("Transaction successful");

        B2CAsyncResponse callbackResponse = new B2CAsyncResponse();
        callbackResponse.setResult(resultObject);

        mockMvc.perform(post("/api/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(callbackResponse)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "message": "Received"
                }
                """));

        ArgumentCaptor<RequestRecord> captor = ArgumentCaptor.forClass(RequestRecord.class);
        verify(recordRepo).save(captor.capture());

        RequestRecord savedRecord = captor.getValue();
        assertNotNull(savedRecord.getDateAccepted());
        assertEquals("SUCCESSFUL", savedRecord.getStatus());
        assertEquals("conv1", savedRecord.getConversationID());
    }

    /**
     * Tests the POST /api/submit-payment endpoint with an invalid amount.
     * Verifies that the endpoint returns a Bad Request status with an appropriate error message.
     */
    @Test
    void payment_withInvalidAmount_returnsBadRequest() throws Exception {
        APIRequest request = new APIRequest();
        request.setAmount(5);  // Invalid amount
        request.setPartyB("254712345678");
        request.setRemarks("Test remarks");
        request.setOccasion("Test occasion");

        mockMvc.perform(post("/api/submit-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                {
                    "message": "Error: Amount must be between 10 and 150000."
                }
                """));
    }

    /**
     * Tests the POST /api/submit-payment endpoint with an invalid phone number.
     * Verifies that the endpoint returns a Bad Request status with an appropriate error message.
     */
    @Test
    void payment_withInvalidPhone_returnsBadRequest() throws Exception {
        APIRequest request = new APIRequest();
        request.setAmount(1000);
        request.setPartyB("0712345678");  // Invalid phone number
        request.setRemarks("Test remarks");
        request.setOccasion("Test occasion");

        mockMvc.perform(post("/api/submit-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                {
                    "message": "Error: Invalid phone number format. It should be a 12-digit Kenyan number starting with 254."
                }
                """));
    }

    /**
     * Tests the POST /api/submit-payment endpoint with empty remarks.
     * Verifies that the endpoint returns a Bad Request status with an appropriate error message.
     */
    @Test
    void payment_withEmptyRemarks_returnsBadRequest() throws Exception {
        APIRequest request = new APIRequest();
        request.setAmount(1000);
        request.setPartyB("254712345678");
        request.setRemarks("");  // Empty remarks
        request.setOccasion("Test occasion");

        mockMvc.perform(post("/api/submit-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                {
                    "message": "Error: Remarks are required."
                }
                """));
    }

    /**
     * Tests the POST /api/submit-payment endpoint with empty occasion.
     * Verifies that the endpoint returns a Bad Request status with an appropriate error message.
     */
    @Test
    void payment_withEmptyOccasion_returnsBadRequest() throws Exception {
        APIRequest request = new APIRequest();
        request.setAmount(1000);
        request.setPartyB("254712345678");
        request.setRemarks("Test remarks");
        request.setOccasion("");  // Empty occasion

        mockMvc.perform(post("/api/submit-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                {
                    "message": "Error: Occasion is required."
                }
                """));
    }

    /**
     * Tests the POST /api/submit-payment endpoint with a valid request.
     * Verifies that the endpoint returns a success message and correctly interacts with the producer.
     */
    @Test
    void payment_withValidRequest_returnsSuccess() throws Exception {
        APIRequest request = new APIRequest();
        request.setAmount(1000);
        request.setPartyB("254712345678");
        request.setRemarks("Test remarks");
        request.setOccasion("Test occasion");

        given(producer.sendB2CRequest(org.mockito.ArgumentMatchers.any())).willReturn(true);

        mockMvc.perform(post("/api/submit-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "message": "Request Sent"
                }
                """));

        ArgumentCaptor<B2CRequest> captor = ArgumentCaptor.forClass(B2CRequest.class);
        verify(producer).sendB2CRequest(captor.capture());

        B2CRequest sentRequest = captor.getValue();
        assertEquals(DarajaUtils.initiatorName, sentRequest.getInitiatorName());
        assertEquals(DarajaUtils.shortCode, sentRequest.getPartyA());
        assertEquals(DarajaUtils.resultURL, sentRequest.getResultURL());
        assertEquals(DarajaUtils.queueTimeOutURL, sentRequest.getQueueTimeOutURL());
    }

    /**
     * Tests the POST /api/submit-payment endpoint with a valid request but producer fails.
     * Verifies that the endpoint returns a failure message when the producer fails to send the request.
     */
    @Test
    void payment_withValidRequest_returnsFailure() throws Exception {
        APIRequest request = new APIRequest();
        request.setAmount(1000);
        request.setPartyB("254712345678");
        request.setRemarks("Test remarks");
        request.setOccasion("Test occasion");

        given(producer.sendB2CRequest(org.mockito.ArgumentMatchers.any())).willReturn(false);

        mockMvc.perform(post("/api/submit-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "message": "Request Not Sent"
                }
                """));
    }
}

package com.kafkab2c.KafkaB2C.Kafka;

import com.kafkab2c.KafkaB2C.Database.RequestRecord;
import com.kafkab2c.KafkaB2C.Database.RequestRecordRepo;
import com.kafkab2c.KafkaB2C.Models.B2CRequest;
import com.kafkab2c.KafkaB2C.Models.B2CSynchronousResponse;
import com.kafkab2c.KafkaB2C.utils.DarajaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Consumer service for handling Kafka messages related to B2C requests and responses.
 */
@Service
public class Consumer {

    private final Producer producer;
    @Autowired
    private RequestRecordRepo recordRepo;
    @Autowired
    private RequestRecordRepo requestRecordRepo;

    /**
     * Constructor for initializing the Consumer service.
     *
     * @param producer The Producer instance used to send Kafka messages.
     */
    public Consumer(Producer producer) {
        this.producer = producer;
    }

    /**
     * Listens to the "B2CRequest" topic and processes incoming B2CRequest messages.
     *
     * @param b2crequest The B2CRequest message received from Kafka.
     */
    @KafkaListener(topics = "B2CRequest", groupId = "consumerGroup")
    public void sendB2CRequest(B2CRequest b2crequest) {
        // Get access token for authentication
        String accessToken = DarajaUtils.getAccessToken();

        // Send B2C request and receive response
        B2CSynchronousResponse response = DarajaUtils.sendB2CRequest(accessToken, b2crequest);

        // Create and save a new RequestRecord in the database
        RequestRecord request = new RequestRecord();
        request.setAmount(b2crequest.getAmount());
        request.setAttemptCount(0);
        request.setConversationID("");
        request.setRemarks(b2crequest.getRemarks());
        request.setOccassion(b2crequest.getOccassion());
        request.setDateCreated(LocalDateTime.now().toString());
        request.setDateAccepted("");
        request.setPhone(b2crequest.getPartyB());
        request.setStatus("SENT");
        request.setConversationID(response.getConversationID());
        recordRepo.save(request);

        // Send updated B2CRequest to Kafka topic
        b2crequest.setB2CSynchronousResponse(response);
        producer.B2CRequestSent(b2crequest);
    }

    /**
     * Listens to the "B2CPending" topic and updates B2CRequest statuses.
     *
     * @param b2crequest The B2CRequest message received from Kafka.
     */
    @KafkaListener(topics = "B2CPending", groupId = "consumerGroup")
    public void updateB2CRequest(B2CRequest b2crequest) {
        // Retrieve all records from the database
        List<RequestRecord> records = recordRepo.findAll();

        // Update status of records with matching conversation ID
        records.forEach(record -> {
            if (record.getStatus().equals("SENT") &&
                    record.getConversationID().equals(b2crequest.getB2CSynchronousResponse().getConversationID())) {
                record.setStatus("PENDING");
                recordRepo.save(record);
            }
        });
    }
}

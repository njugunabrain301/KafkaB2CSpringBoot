package com.kafkab2c.KafkaB2C.API;

import com.kafkab2c.KafkaB2C.Database.RequestRecord;
import com.kafkab2c.KafkaB2C.Database.RequestRecordRepo;
import com.kafkab2c.KafkaB2C.Kafka.Producer;
import com.kafkab2c.KafkaB2C.Models.*;
import com.kafkab2c.KafkaB2C.utils.DarajaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
/*
 * The main REST controller for handling API requests related to B2C payments.
 */
public class API {
    private Producer producer;

    @Autowired
    private RequestRecordRepo recordRepo;

    public API(Producer producer) {
        this.producer = producer;
    }

    /**
     * Handles the submission of a payment request.
     *
     * @param request The payment request details.
     * @return A ResponseEntity containing the result of the request.
     */
    @PostMapping("/submit-payment")
    public ResponseEntity<APIResponse> publish(@RequestBody APIRequest request){
        // Validate amount
        if (request.getAmount() < 10 || request.getAmount() > 150000) {
            return ResponseEntity.badRequest().body(new APIResponse("Error: Amount must be between 10 and 150000."));
        }

        // Validate phone number
        String partyB = request.getPartyB();
        if (!partyB.startsWith("254") || partyB.length() != 12 || !partyB.matches("\\d+")) {
            return ResponseEntity.badRequest().body(new APIResponse("Error: Invalid phone number format. It should be a 12-digit Kenyan number starting with 254."));
        }

        // Validate remarks
        if (request.getRemarks() == null || request.getRemarks().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new APIResponse("Error: Remarks are required."));
        }

        // Validate occasion
        if (request.getOccasion() == null || request.getOccasion().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new APIResponse("Error: Occasion is required."));
        }

        // Create and populate B2CRequest
        B2CRequest b2crequest = new B2CRequest(request);
        b2crequest.setInitiatorName(DarajaUtils.initiatorName);
        b2crequest.setPartyA(DarajaUtils.shortCode);
        b2crequest.setResultURL(DarajaUtils.resultURL);
        b2crequest.setQueueTimeOutURL(DarajaUtils.queueTimeOutURL);

        // Send B2C request
        boolean res = producer.sendB2CRequest(b2crequest);
        if(res)
            return ResponseEntity.ok(new APIResponse("Request Sent"));
        else
            return ResponseEntity.ok(new APIResponse("Request Not Sent"));
    }

    /**
     * Handles the callback for the payment request.
     *
     * @param callback The callback details from the payment request.
     * @return A ResponseEntity indicating the receipt of the callback.
     */
    @PostMapping("/callback")
    public ResponseEntity<APIResponse> callback(@RequestBody B2CAsyncResponse callback){
        List<RequestRecord> records = recordRepo.findAll();
        records.forEach(record -> {
            if(callback.getResult().getConversationID().equals(record.getConversationID())) {
                if (callback.getResult().getResultCode() == 0) {
                    record.setStatus("SUCCESSFUL");
                    record.setDateAccepted(LocalDateTime.now().toString());
                    recordRepo.save(record);
                } else {
                    record.setStatus("FAILED");
                    record.setDateAccepted(LocalDateTime.now().toString());
                    recordRepo.save(record);
                }
            }
        });

        return ResponseEntity.ok(new APIResponse("Received"));
    }

    /**
     * Handles the timeout for the payment request.
     *
     * @param timeout The timeout details from the payment request.
     * @return A ResponseEntity indicating the receipt of the timeout.
     */
    @PostMapping("/timeout")
    public ResponseEntity<APIResponse> timeout(@RequestBody B2CTimeOutResponse timeout){
        List<RequestRecord> records = recordRepo.findAll();
        records.forEach(record -> {
            if(timeout.getResult().getConversationID().equals(record.getConversationID())) {
                if (timeout.getResult().getResultCode() == 0) {
                    record.setStatus("SUCCESSFUL");
                    record.setDateAccepted(LocalDateTime.now().toString());
                    recordRepo.save(record);
                } else {
                    record.setStatus("FAILED");
                    record.setDateAccepted(LocalDateTime.now().toString());
                    recordRepo.save(record);
                }
            }
        });

        return ResponseEntity.ok(new APIResponse("Received"));
    }

    /**
     * Retrieves all request records.
     *
     * @return A ResponseEntity containing the list of request records.
     */
    @GetMapping("/records")
    public ResponseEntity<APIResponse> getRecords(){
        List<RequestRecord> records = recordRepo.findAll();
        return ResponseEntity.ok(new APIResponse("Success", records));
    }
}

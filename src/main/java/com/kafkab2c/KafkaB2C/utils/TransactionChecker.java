package com.kafkab2c.KafkaB2C.utils;

import com.kafkab2c.KafkaB2C.Database.RequestRecord;
import com.kafkab2c.KafkaB2C.Database.RequestRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TransactionChecker class periodically checks the status of pending transactions.
 */
@Component
public class TransactionChecker {

    /** Repository for accessing request records. */
    @Autowired
    private RequestRecordRepo recordRepo;

    /**
     * Scheduled task to check the status of pending transactions every 5 minutes.
     * Updates the attempt count and saves the record if the status is "PENDING".
     */
    @Scheduled(fixedRate = 300000) // Executes every 5 minutes
    public void performTask() {
        // Retrieve all records from the database
        List<RequestRecord> records = recordRepo.findAll();
        // Iterate through each record and update status if pending
        records.forEach(record -> {
            if (record.getStatus().equals("PENDING") && record.getConversationID().length() > 0) {
                // Increment attempt count
                record.setAttemptCount(record.getAttemptCount() + 1);
                // Check transaction status
                DarajaUtils.checkTransactionStatus(record.getConversationID());
                // Save the updated record
                recordRepo.save(record);
            }
        });
    }
}

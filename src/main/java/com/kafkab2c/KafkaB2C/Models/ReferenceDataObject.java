package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * ReferenceDataObject class holds reference data related to a B2C transaction.
 */
@Data
public class ReferenceDataObject {

    /** The reference item associated with the B2C transaction. */
    private ReferenceItem referenceItem;
}

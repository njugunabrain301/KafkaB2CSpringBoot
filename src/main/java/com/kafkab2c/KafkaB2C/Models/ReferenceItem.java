package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * ReferenceItem class represents a key-value pair used in B2C transactions.
 */
@Data
public class ReferenceItem {

    /** The value associated with the reference item. */
    private String Value;

    /** The key associated with the reference item. */
    private String Key;
}

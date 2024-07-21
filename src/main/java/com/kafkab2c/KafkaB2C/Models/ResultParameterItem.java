package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * ResultParameterItem class represents an individual parameter associated with a transaction result.
 */
@Data
public class ResultParameterItem {

    /** The value of the result parameter. */
    private String Value;

    /** The key associated with the result parameter. */
    private String Key;
}

package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;
import java.util.List;

/**
 * ResultParametersObject class represents a collection of result parameters associated with a transaction.
 */
@Data
public class ResultParametersObject {

    /** A list of result parameter items. */
    private List<ResultParameterItem> ResultParameter;
}

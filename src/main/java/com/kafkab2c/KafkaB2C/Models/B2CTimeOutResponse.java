package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * B2CTimeOutResponse class represents the response received when a B2C transaction times out.
 */
@Data
public class B2CTimeOutResponse {

        /** The result of the timeout response. */
        private TimeOutResult Result;
}

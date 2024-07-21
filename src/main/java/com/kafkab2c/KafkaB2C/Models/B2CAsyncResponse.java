package com.kafkab2c.KafkaB2C.Models;

import lombok.Data;

/**
 * B2CAsyncResponse class represents the structure of the asynchronous response
 * received from a B2C request.
 */
@Data
public class B2CAsyncResponse {

        /**
         * Result object containing details of the asynchronous response.
         */
        private ResultObject Result;
}

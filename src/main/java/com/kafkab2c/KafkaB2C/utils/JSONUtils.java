package com.kafkab2c.KafkaB2C.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

/**
 * JSONUtils class provides utility methods for working with JSON data.
 */
public class JSONUtils {

    /** ObjectMapper instance used for JSON processing. */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a JSON string into a Map.
     *
     * @param jsonString The JSON string to be converted.
     * @return A Map representation of the JSON string.
     * @throws IOException If there is an error processing the JSON string.
     */
    public static Map<String, Object> jsonToMap(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, Map.class);
    }
}

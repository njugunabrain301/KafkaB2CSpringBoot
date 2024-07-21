package com.kafkab2c.KafkaB2C.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSONUtilsTest is a test class for the JSONUtils utility class.
 * It contains test methods to verify the functionality of various utility methods in JSONUtils.
 */
class JSONUtilsTest {

    /**
     * Tests the jsonToMap method in JSONUtils.
     * Verifies that the method correctly converts JSON strings to maps and retrieves values by keys.
     */
    @Test
    public void jsonToMap() {
        try {
            // Test with a simple JSON string containing one key-value pair
            Map<String, Object> map = JSONUtils.jsonToMap("{\"name\":\"Brian\"}");
            assertEquals("Brian", map.get("name"));

            // Test with a JSON string containing multiple key-value pairs
            map = JSONUtils.jsonToMap("{\"name\":\"Brian\", \"age\": 27}");
            assertEquals("Brian", map.get("name"));
            assertEquals(27, map.get("age"));

        } catch (IOException e) {
            // Fail the test if an IOException occurs
            fail();
        }
    }
}

package org.testClient.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper OBJECT_MAPPER = (new ObjectMapper()).registerModule(new JavaTimeModule());

    private JsonHelper() {
    }

    public static String writeValueAsString(Object value) {
        return writeValueAsString(value, OBJECT_MAPPER);
    }

    public static String writeValueAsString(Object value, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException var3) {
            LOGGER.error("Error processing object", var3);
            return "Could not marshall " + value;
        }
    }


}

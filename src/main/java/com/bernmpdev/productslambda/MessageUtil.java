package com.bernmpdev.productslambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MessageUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String returnMessage(String message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("message", message));
    }
}

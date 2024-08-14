package com.bernmpdev.productslambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ExceptionHandler {

    public static APIGatewayProxyResponseEvent handleException(Exception e) {
        System.err.println("An error occurred: " + e.getMessage());
        e.printStackTrace();

        if (e instanceof IllegalArgumentException) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("Bad request: " + e.getMessage());
        } else if (e instanceof JsonProcessingException) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error processing JSON: " + e.getMessage());
        } else {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("An internal error occurred: " + e.getMessage());
        }
    }
}

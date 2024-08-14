package com.bernmpdev.productslambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ResponseUtil {

    public static APIGatewayProxyResponseEvent createResponse(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(body);
    }
}

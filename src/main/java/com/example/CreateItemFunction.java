package com.example;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CreateItemFunction {
    @FunctionName("createItem")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Item> request,
            final ExecutionContext context) {
        
        context.getLogger().info("Java HTTP trigger processed a request to create an item.");

        // Simulate long network and database latency without sending any response headers
        try {
            TimeUnit.SECONDS.sleep(120); // 120 seconds to ensure browser timeout
        } catch (InterruptedException e) {
            context.getLogger().warning("Sleep interrupted: " + e.getMessage());
        }

        // This response will never be sent because the client will have timed out
        Item item = request.getBody();
        if (item == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Please pass an item in the request body")
                    .build();
        }

        return request.createResponseBuilder(HttpStatus.OK)
                .body(item)
                .build();
    }
} 
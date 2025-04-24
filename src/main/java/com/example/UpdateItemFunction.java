package com.example;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UpdateItemFunction {
    @FunctionName("updateItem")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.PUT},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<Item>> request,
            final ExecutionContext context) {
        
        context.getLogger().info("Java HTTP trigger processed a request to update an item.");

        // Get id from query parameters
        String id = request.getQueryParameters().get("id");

        // Simulate long network and database latency
        try {
            TimeUnit.SECONDS.sleep(60); // Fixed 60 second delay
        } catch (InterruptedException e) {
            context.getLogger().warning("Sleep interrupted: " + e.getMessage());
        }

        if (id == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Please pass an id on the query string")
                    .build();
        }

        Optional<Item> itemOpt = request.getBody();
        if (!itemOpt.isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Please pass an item in the request body")
                    .build();
        }

        Item item = itemOpt.get();
        item.setId(id);

        // Simulate database operation
        return request.createResponseBuilder(HttpStatus.OK)
                .body(item)
                .build();
    }
} 
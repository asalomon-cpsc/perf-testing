package com.example;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DeleteItemFunction {
    @FunctionName("deleteItem")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.DELETE},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        
        context.getLogger().info("Java HTTP trigger processed a request to delete an item.");

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

        // Simulate database operation
        return request.createResponseBuilder(HttpStatus.OK)
                .body("Item with ID " + id + " deleted")
                .build();
    }
} 
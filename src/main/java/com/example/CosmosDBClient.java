package com.example;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.UUID;

public class CosmosDBClient {
    private static final String DATABASE_ID = "crud-db";
    private static final String CONTAINER_ID = "items";
    private static CosmosDBClient instance;
    private final CosmosContainer container;

    private CosmosDBClient() {
        String endpoint = System.getenv("COSMOS_ENDPOINT");
        String key = System.getenv("COSMOS_KEY");

        CosmosClient client = new CosmosClientBuilder()
                .endpoint(endpoint)
                .key(key)
                .buildClient();

        CosmosDatabase database = client.getDatabase(DATABASE_ID);
        this.container = database.getContainer(CONTAINER_ID);
    }

    public static synchronized CosmosDBClient getInstance() {
        if (instance == null) {
            instance = new CosmosDBClient();
        }
        return instance;
    }

    public Item createItem(Item item) {
        if (item.getId() == null) {
            item.setId(UUID.randomUUID().toString());
        }
        CosmosItemResponse<Item> response = container.createItem(item);
        return response.getItem();
    }

    public Item readItem(String id) {
        try {
            CosmosItemResponse<Item> response = container.readItem(id, new PartitionKey(id), Item.class);
            return response.getItem();
        } catch (Exception e) {
            return null;
        }
    }

    public Item updateItem(Item item) {
        CosmosItemResponse<Item> response = container.replaceItem(item, item.getId(), new PartitionKey(item.getId()), new CosmosItemRequestOptions());
        return response.getItem();
    }

    public void deleteItem(String id) {
        container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
    }

    public CosmosPagedIterable<Item> getAllItems() {
        return container.queryItems("SELECT * FROM c", new CosmosQueryRequestOptions(), Item.class);
    }
} 
# Azure Functions CRUD Operations Test

This project demonstrates CRUD operations using Azure Functions with simulated database latency.

## Prerequisites

- Java 11
- Maven
- Azure CLI
- Azure Functions Core Tools

## Local Development

1. Build the project:
```bash
mvn clean package
```

2. Run the function locally:
```bash
mvn azure-functions:run
```

3. Open the frontend:
- Navigate to `src/main/web/index.html` in your browser
- The frontend will connect to the local function app running on `http://localhost:7071`

## Deployment

### Deploy Function App

1. Login to Azure:
```bash
az login
```

2. Deploy the function app:
```bash
mvn azure-functions:deploy
```

### Deploy Static Web App

1. Create a storage account for the static web app:
```bash
az storage account create \
    --name spateststor \
    --resource-group spa-test-rg \
    --location eastus \
    --sku Standard_LRS \
    --kind StorageV2
```

2. Enable static website hosting:
```bash
az storage blob service-properties update \
    --account-name spateststor \
    --static-website \
    --404-document index.html \
    --index-document index.html
```

3. Upload the frontend files:
```bash
az storage blob upload-batch \
    --account-name spateststor \
    --source src/main/web \
    --destination '$web' \
    --content-type 'text/html'
```

## Testing

The application provides a simple web interface to test all CRUD operations:
- Create: Create a new item with name and description
- Read: Retrieve an item by ID
- Update: Update an existing item's name and description
- Delete: Remove an item by ID

Each operation simulates a database call with a 30-40 second latency. The frontend will handle timeouts gracefully.

## Notes

- The function app is configured to run on the consumption plan
- CORS is enabled for local development
- The frontend is deployed as a static website in Azure Storage 
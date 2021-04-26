# User Manual

In order to test the application ensure that the mock server created in section [Installation Guide](installationguide.md) is up and running,
as well as the four docker images created and initialized in section [Getting Started](getting-started.md).

## Test CoFFEE

Let's test the integration between CoFFEE and ERP (mock server).
Firstly ensure that all warehouses and workstations used in mock server are created in Orion. In the `warehouses` and `workstations` requests, adapt the fields `name` and `erpId`.

To create warehouse(s) use:

```console
curl --request POST 'localhost:8080/api/broker/warehouse' \
--header 'Content-Type: application/json' \
--data-raw '{
    "latitude": 41.14961,
    "longitude": -8.61099,
    "status": "ready",
    "name": "Warehouse 1",
    "erpId": "0007"
}'
```

It should respond with the Orion entity's id, for example: 

    {"orionId":"urn:ngsi-ld:Warehouse:fe301f896ba2473d8e07f2b8f07c2561"}

To create workstation(s) use:

```console
curl --request POST 'localhost:8080/api/broker/station/work' \
--header 'Content-Type: application/json' \
--data-raw '{
    "latitude": 41.14961,
    "longitude": -8.61099,
    "status": "ready",
    "name": "K50_1",
    "erpId": "0161"
}'
```

The response should be similar, again with the Orion entity's id, for example: 

    {"orionId":"urn:ngsi-ld:Workstation:6a00d21b64634dbfacd446ed6483194c"}

To integrate work orders execute:

```console
curl -i --request POST 'http://localhost:8081/api/coffee/workorder/integrate'
```

It will print to standard output the Orion IDs of the work orders integrated. Example:

    {
        "errors": [],
        "workOrderIds": [
            "urn:ngsi-ld:WorkOrder:9d2105b0b7c046eeab0c10eaf2c68566",
            "urn:ngsi-ld:WorkOrder:84fc28794d9d4f0cb8d0caeda4b44a62",
            "urn:ngsi-ld:WorkOrder:2f6cad2689634769b32860f466dd8cdb"
        ]
    }

This is already a good signal :) But let's ensure that the work orders were integrated correctly in Orion.

```console
curl 'http://localhost:1026/v2/entities?type=WorkOrder&attrs=id' | python -mjson.tool
```

    [
        {
            "id": "urn:ngsi-ld:WorkOrder:9d2105b0b7c046eeab0c10eaf2c68566",
            "type": "WorkOrder"
        },
        {
            "id": "urn:ngsi-ld:WorkOrder:84fc28794d9d4f0cb8d0caeda4b44a62",
            "type": "WorkOrder"
        },
        {
            "id": "urn:ngsi-ld:WorkOrder:2f6cad2689634769b32860f466dd8cdb",
            "type": "WorkOrder"
        }
    ]

This means that the work orders were correctly integrated because they are present in Orion.
You can remove the query parameter `attrs=id` to check the complete work order information.

```console
curl 'http://localhost:1026/v2/entities?type=WorkOrder' | python -mjson.tool
```

And, that's it!

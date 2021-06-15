# API

CoFFEE exposes a REST web service that is invoked automatically every day (through [Spring Scheduler](https://spring.io/guides/gs/scheduling-tasks/)) to integrate the work orders from SAP.
This service can also be invoked manually, if it is performed some change in SAP after the integration time range, to ensure that Orion contains always the most updated information.

# REST API

## Integrate work orders

This service consumes SAP work orders, parses the response received, and:
- analyze if the warehouse to retrieve material is known by system, otherwise ignores the work order;
- analyze if the work station to execute the work order is known by system, otherwise ignore the work order;
- create/update work orders (with the respective material and quantity) in Orion.
- call LATTEE to schedule the work orders execution.

The service workflow ignores the work order if do not knows the warehouse and/or the work station because this means that the system do not knows the GPS coordinate to where send the Robot.

The service returns as response an array of IDs of the work orders created in Orion, and an array of errors. This array of errors contains for each case the error message and the respective work order associated.

### Request

`POST /api/coffee/workorder/integrate`

    curl -i --request POST 'http://localhost:8081/api/coffee/workorder/integrate'

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Date: Wed, 14 Oct 2020 10:33:04 GMT

    {
        "errors": [],
        "workOrderIds": [
            "urn:ngsi-ld:WorkOrder:9d2105b0b7c046eeab0c10eaf2c68566",
            "urn:ngsi-ld:WorkOrder:84fc28794d9d4f0cb8d0caeda4b44a62",
            "urn:ngsi-ld:WorkOrder:2f6cad2689634769b32860f466dd8cdb"
        ]
    }

[Next (Installation Guide)](installationguide.md)

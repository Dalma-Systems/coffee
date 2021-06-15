# Architecture

CoFFEE is a component of FEATS system, and his main goal is integrate with ERPs. Currently it is only supported integrate with [SAP](https://www.sap.com).
CoFFEE integrate work orders instructions from ERPs, convert them into internal business models, and stores all information in Orion Context Broker.
The following picture shows the architecture of all FEATS system, where CoFFEE is integrated:

<img src="images/feats.png" width="1024" height="600"/>

## FEATS Components Architecture

In order to easily integrate any FEATS component with Orion Context Broker, it was created an api that works as proxy between all components and Orion. This means that all components of FEATS system (CoFFEE, LATTE and FI-BREW) in order to communicate with Orion, they communicate with the broker api (proxy), that converts internal FEATS business models into Orion models. This Orion models are the [FIWARE's Smart Data Models](https://www.fiware.org/developers/smart-data-models).

## Code Architecture

All FEATS components are Java Spring Boot applications, and the code is always organized with the following layers:
- web
- service
- interface
- base

The "web" layer is where the REST endpoints are exposed. The "service" layer contains all the business logic. The "interface" layer contains all the internal and external [DTOs](https://java-design-patterns.com/patterns/data-transfer-object/). And the "base" layer contains all base classes and plugins configurations usefull for the remain layers.

### Broker Api

The broker api source code (`broker-api`) is structured into five Java projects:
- web
- service
- interface
- fiware
- base

The "fiware" layer contains all the logic to communicate with Orion. Contains all the Smart Data Models used by FEATS system, between them the warehouse, material, workstation and work orders (all used by CoFFEE), and also the connector to Orion, plublishers and subscribers.
The Orion connector is where all the HTTP calls to Orion are performed (regardless the HTTP method). The publisher and subscriber define all the instructions to write/read to/from Orion.

This api currently is an independent component, but can easily be inserted inside CoFFEE has just one component (through [maven dependency](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)). It was structured in this way, as an independent component, to have the possibility of independent deployment. In this way it is possible to implement some improvement in broker api, and all FEATS components automatically have that improvement deploying just this api - avoid the need of deploy all components, one by one.

### CoFFEE Api

The CoFFEE api source code (`coffee-api`) is also structured into five Java projects:
- web
- service
- interface
- erp
- base

The "erp" layer contains all the logic to integrate with ERPs. To easily support integrate with more ERPs, it was implemented the [Factory design pattern](https://java-design-patterns.com/patterns/factory/). This design pattern consists in "hide the implementation logic and make client code focus on usage rather then initialization".

[Next (API)](api.md)

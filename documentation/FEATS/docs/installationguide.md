# Installation Guide

## Install Java

First, you need to install Java 11. If you don't have it installed, visit [Java's download page](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
Once installed ensure that is in your PATH checking Java version.

```console
java -version
```

## Install Maven

If you don't have it installed, visit [Maven's download page](http://maven.apache.org/download.cgi)
Once installed ensure that is in your PATH checking maven version.

```console
mvn -version
```

## Install Docker

Again, if you don't have it installed, visit [Docker's download page](https://docs.docker.com/get-docker/)
Once installed ensure that is in your PATH checking docker version.

```console
docker version
```

## Compile FEATS common projects

Change to `src` directory and compile each common project: `commons` and `contracts`

```console
cd src/commons
mvn clean install
```

```console
cd src/contracts
mvn clean install
```

## Compile CoFFEE project

Before compile CoFFEE source code it is necessary adapt three properties in order to components communicate with each other. Two properties are adapted through the following shell commands.

Change to `src` directory and execute:

```console
sed -i -e "s/^dalma.broker.api.url.*/dalma.broker.api.url=http:\/\/broker-api:8090/" coffee/coffee-api-web/src/main/resources/application-local.properties
sed -i -e "s/^fiware.orion.host.*/fiware.orion.host=orion/" broker/broker-api-web/src/main/resources/application-local.properties
```

The third property needs to be changed manually. Firstly, you need to create a XML mock server to emulate SAP response. You can use for example [Postman](https://www.postman.com/downloads/) and [configure a mock server](https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/). The mock server should expose a GET web service (without any header) and the example of the response is available [here](examples/mock_server.xml). After get the URL of the mocked web service, insert it in the property `erp.work.orders.url` of properties file `src/coffee/coffee-api-web/src/main/resources/application-local.properties`.

After that compile each component `broker` and `coffee` of `src` directory.

```console
cd broker
mvn clean install
```

```console
cd coffee
mvn clean install
```

[Next (Getting Started)](getting-started.md)

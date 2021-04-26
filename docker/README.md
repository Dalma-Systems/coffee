# Dockerfiles

## Build images of Broker API and CoFFEE API

The [Broker Dockerfile](broker/Dockerfile) and [CoFFEE Dockerfile](coffee/Dockerfile) will be used to build docker images:

```console
cd src/broker
docker build -f ../../docker/broker/Dockerfile -t broker-api .
```

```console
cd src/coffee
docker build -f ../../docker/coffee/Dockerfile -t coffee-api .
```

[Back to (Getting Started)](../docs/getting-started.md)

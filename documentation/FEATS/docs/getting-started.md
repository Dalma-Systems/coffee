# Getting Started

Contents:
- [Orion Installation](#orion-installation)
- [Dockerize CoFFEE](#dockerize-coffee)

First of all let's create a docker network in order to containers communicate with each other

```console
docker network create feats
```

## Orion Installation

Since CoFFEE is a integrator of information to Orion, you need also to install Orion. We are going to use docker images
to keep it simple.

```console
docker run --network feats --name mongodb -d mongo:3.4
docker run -d --network feats --name orion --link mongodb:mongodb -p 1026:1026 fiware/orion -dbhost mongodb
```

Check that everything works with

```console
curl localhost:1026/version
```

Should print to standard output something similar to

    {
    "orion" : {
      "version" : "2.4.0-next",
      "uptime" : "0 d, 23 h, 42 m, 7 s",
      "git_hash" : "4bf835364feec277dbcd5e146f4077494151e3c9",
      "compile_time" : "Tue Jul 28 11:30:53 UTC 2020",
      "compiled_by" : "root",
      "compiled_in" : "deaf7dfffff1",
      "release_date" : "Tue Jul 28 11:30:53 UTC 2020",
      "doc" : "https://fiware-orion.rtfd.io/"
    }
    }

## Dockerize CoFFEE

To create the required CoFFEE Docker images follow the [README](../docker/README.md) instructions in `docker` folder.  
Then execute the docker images created.

```console
docker run -d --network feats --name broker-api -p 8080:8090 broker-api
docker run -d --network feats --name coffee-api -p 8081:8092 coffee-api
```

Wait a few seconds and check that broker-api is up and working

```console
curl http://localhost:8080/info | python -mjson.tool
```

    {
        "build": {
            "artifact": "dalma-broker-web",
            "group": "com.dalma.broker",
            "name": "dalma-broker-web",
            "time": "2020-10-15T09:46:21.385Z",
            "version": "1.0-SNAPSHOT"
        }
    }

Confirm also that coffee-api is up and working

```console
curl http://localhost:8081/info | python -mjson.tool
```

    {
        "build": {
            "artifact": "dalma-coffee-web",
            "group": "com.dalma.coffee",
            "name": "dalma-coffee-web",
            "time": "2020-10-15T09:46:41.482Z",
            "version": "1.0-SNAPSHOT"
        }
    }

Confirm communication between the docker images broker-api, Orion and mongo

```console
curl http://localhost:8080/api/broker/workorder | python -mjson.tool
```

    []

[Next (User Manual)](usermanual.md)

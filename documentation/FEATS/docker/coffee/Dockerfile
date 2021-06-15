FROM openjdk:11-jdk-slim

RUN apt-get update && apt-get -y install curl && apt-get clean

ADD coffee-api-web/target/dalma-coffee-api.jar /app/

EXPOSE 8080

ENTRYPOINT ["java","-jar", "/app/dalma-coffee-api.jar"]

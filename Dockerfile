## Build stage ##
FROM maven:3.9.9-eclipse-temurin-21 as deps

WORKDIR /app

COPY pom.xml /app

RUN mvn dependency:go-offline

FROM maven:3.9.9-eclipse-temurin-21 as build

WORKDIR /app

COPY src /app
COPY --from=deps /root/.m2 /root/.m2

RUN mvn clean package -Dmaven.test.skip=true

## Run stage ##
FROM openjdk:23 as run

WORKDIR /app

RUN mkdir -p /app/uploads && mkdir -p /app/config && mkdir -p /app/log

COPY --from=build /app/target/*.jar /app/karaoke.jar

RUN chown -R 1000:1000 /app

USER 1000

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/qiqishop.jar","--spring.config.name=application","--spring.config.location=/app/config/application.properties"]

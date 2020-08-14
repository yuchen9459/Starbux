FROM openjdk:8-jdk-alpine

ENV LOADER_PATH="/home/app/conf"

EXPOSE 8080

ARG JAR_FILE=target/*.jar
ARG CONFIG_FILE=config

COPY ${JAR_FILE} app.jar
ADD ${CONFIG_FILE} .

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
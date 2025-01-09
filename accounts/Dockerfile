#start with base image containg java runtime
FROM openjdk:17-jdk-slim

#information around who maintains the image
LABEL org.opencontainers.image.authors="whojayantkumar"

# Add application's jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#Executes the application
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]


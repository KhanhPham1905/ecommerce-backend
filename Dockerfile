# syntax = docker/dockerfile:1
FROM openjdk:24-oraclelinux8
#working directory
WORKDIR /app
#copy from your Host(PC, laptop) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#run this inside the image
RUN ./mvnw dependency:go-offline
COPY src ./src

#run inside container
CMD ["./mvnw", "spring-boot:run"]
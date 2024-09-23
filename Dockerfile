###
#Build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY ./ /app/shopapp-backend
RUN mvn package -f /app/shopapp-backend/pom.xml
RUN mkdir -p /app/shopapp-backend/uploads
#multi-staging
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/shopapp-backend/target/*.jar app.jar
COPY --from=build /app/shopapp-backend/uploads uploads

EXPOSE 3000
CMD ["java","-jar","app.jar"]

#docker build -t shopapp-spring:1.0.4 -f ./DockerfileJavaSpring .
#docker login
#create khanhquocphamdev/shopapp-spring:1.0.0 repository on DockerHub
#docker tag shopapp-spring:1.0.0 khanhquocphamdev/shopapp-spring:1.0.0
#docker push khanhquocphamdev/shopapp-spring:1.0.0

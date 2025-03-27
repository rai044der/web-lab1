FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/cars-business-card-0.0.1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:17-ea-10-jdk-slim
WORKDIR /app
COPY target/currency-rate-service-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
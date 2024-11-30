FROM openjdk:17-ea-10-jdk-slim
WORKDIR /app
COPY build/libs/currency-rate-service-0.0.1.jar app.jar
CMD ["java", "-jar", "app.jar"]
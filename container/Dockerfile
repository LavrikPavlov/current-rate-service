FROM openjdk:17-ea-10-jdk-slim
WORKDIR /app
COPY ../container/target/currency-rate-container-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
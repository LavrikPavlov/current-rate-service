FROM openjdk:17-jdk-slim
WORKDIR /app
COPY service/target/currency-rate-service-0.0.1-SNAPSHOT.jar /app/currency-rate-service.jar
CMD ["sh", "-c", "exec java -jar currency-rate-service.jar"]

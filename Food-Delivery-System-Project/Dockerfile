# Stage 1: Build the application

FROM maven:4.0.0-openjdk-17 AS builder

WORKDIR /app

# Copy the pom.xml and source code

COPY pom.xml .

COPY src ./src

# Build the application (skipping tests for speed)

RUN mvn clean package -DskipTests
 
# Stage 2: Create the runtime image

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the JAR from the builder stage

COPY --from=builder /app/target/*.jar app.jar
 
# Expose the application port

EXPOSE 8085
 
# Run the application

ENTRYPOINT ["java", "-jar", "app.jar"]
 
# Stage 1: The Build Environment
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: The Production Environment (Modern Java 21 image)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy ONLY the compiled .jar file
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
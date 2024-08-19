# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application jar file to the container
COPY target/spring-boot-automated-testing.jar /app/spring-boot-automated-testing.jar

# Expose the port that the application runs on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app/spring-boot-automated-testing.jar"]

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host machine to the Docker image
COPY target/tournament-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which your Spring Boot app will run
EXPOSE 8083

# Run the JAR file
ENTRYPOINT ["java","-jar","/app/app.jar"]
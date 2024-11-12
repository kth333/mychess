# Stage 1: Build the Spring Boot application JAR
FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

# Copy the JAR file from the build context
COPY target/match-service-0.0.1-SNAPSHOT.jar app.jar

# Stage 2: NGINX with Spring Boot
FROM nginx:alpine

# Install dependencies
RUN apk add --no-cache openjdk17-jre curl

# Create necessary directories for NGINX and SSL
RUN mkdir -p /app /etc/nginx/certs

# Copy the JAR from the builder stage
COPY --from=builder /app/app.jar /app/app.jar

# Copy SSL certificates and custom NGINX configuration
COPY ./nginx/certs/fullchain.pem /etc/nginx/certs/fullchain.pem
COPY ./nginx/certs/privkey.pem /etc/nginx/certs/privkey.pem

# Expose NGINX (443 for HTTPS) and the Spring Boot app
EXPOSE 443 8082

# Run Spring Boot and NGINX in parallel
CMD java -jar /app/app.jar --spring.profiles.active=docker &
CMD ["nginx", "-g", "daemon off;"]
# Stage 1: Build the Spring Boot application JAR
FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

# Copy the JAR file from the build context
COPY target/match-service-0.0.1-SNAPSHOT.jar app.jar

# Stage 2: NGINX with Supervisord and Spring Boot
FROM nginx:alpine

# Install dependencies
RUN apk add --no-cache openjdk17-jre curl supervisor

# Create necessary directories for NGINX and SSL
RUN mkdir -p /app /etc/nginx/certs /etc/supervisor.d

# Copy the JAR from the builder stage
COPY --from=builder /app/app.jar /app/app.jar

# Copy SSL certificates (added in CI/CD pipeline)
COPY ./nginx/certs/fullchain.pem /etc/nginx/certs/fullchain.pem
COPY ./nginx/certs/privkey.pem /etc/nginx/certs/privkey.pem

# Copy custom NGINX configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Supervisor configuration to run both NGINX and the Spring Boot app
COPY supervisord.conf /etc/supervisor.d/supervisord.conf

# Expose NGINX (443 for HTTPS) and the Spring Boot app
EXPOSE 443 8082

# Start both services with supervisord
CMD ["supervisord", "-c", "/etc/supervisor.d/supervisord.conf"]
# Use an official Node.js runtime as a parent image for the build stage
FROM node:18 AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install the app dependencies
RUN npm install

# Copy the app source code to the container
COPY . .

# Build the app for production
RUN npm run build

# Use an official NGINX image to serve the app
FROM nginx:alpine

# Remove default NGINX configuration file that conflicts
RUN rm /etc/nginx/conf.d/default.conf

# Copy the build folder from the previous stage to NGINX's public folder
COPY --from=build /app/build /usr/share/nginx/html

# Copy custom NGINX configuration
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf

# Expose the port on which NGINX will run
EXPOSE 80

# Start NGINX
CMD ["nginx", "-g", "daemon off;"]
# Use Ubuntu as the base image
FROM ubuntu:latest

# Set author label
LABEL authors="aarya"

# Install dependencies
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk dos2unix

# Set environment variables
ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Set the working directory
WORKDIR /app

# Copy the application files
COPY . /app

# Make Gradlew executable
RUN dos2unix gradlew && \
    chmod +x gradlew

# Copy the pre-built JAR file to the runtime directory
COPY build/libs/*.jar /run/server.jar

# Expose the port
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "/run/server.jar"]
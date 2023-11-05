# Use an Alpine-based image with OpenJDK 17
FROM adoptopenjdk:17-jdk-hotspot AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY TalDate/build/libs/TalDate.jar /app/app.jar

# Expose the port your application runs on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]
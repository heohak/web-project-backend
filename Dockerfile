FROM openjdk:17-jdk AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the module into the container
COPY TalDate/build/libs/TalDate.jar /app/app.jar

# Expose the port your application runs on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]
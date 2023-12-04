FROM eclipse-temurin:17-jdk-alpine AS build
COPY TalDate/ /builder
WORKDIR /builder
RUN ./gradlew clean build

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /builder/build/libs/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
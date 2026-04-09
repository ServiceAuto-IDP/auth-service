FROM maven:3.9.6-eclipse-temurin-21 AS build
# Create the folder and copy the locally developed auth-service
WORKDIR /app
COPY . .
# Run to create the .jar file
RUN mvn clean package -DskipTests

FROM alpine:edge
# Install java 21
RUN apk update && apk add openjdk21-jre
# Go in the app folder and copy the .jar file from the initial build stage
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
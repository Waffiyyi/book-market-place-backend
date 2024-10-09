FROM maven:3.8.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/Online-Food-Ordering-0.0.1-SNAPSHOT.jar Online-Food-Ordering-0.0.1-SNAPSHOT.jar

EXPOSE 8011

ENTRYPOINT ["java", "-jar", "Online-Food-Ordering-0.0.1-SNAPSHOT.jar"]

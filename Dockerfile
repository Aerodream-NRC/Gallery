FROM gradle:8.6.0-jdk21 AS builder
WORKDIR /app

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/

COPY src ./src

RUN chmod +x gradlew

RUN ./gradlew clean build -x test --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
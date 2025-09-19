FROM openjdk:21-jdk-slim

WORKDIR /app

COPY gradlew gradlew.bat ./
COPY gradle gradle/
COPY build.gradle settings.gradle ./
COPY src src/

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "build/libs/Team-IT-0.0.1-SNAPSHOT.jar"]
FROM gradle:8.9-jdk17-alpine AS build-stage
WORKDIR /hospital-appointments
COPY build.gradle settings.gradle gradlew gradlew.bat gradle/ ./
COPY src ./src
RUN gradle build

FROM openjdk:17-jdk-alpine
WORKDIR /hospital-appointments
COPY --from=build-stage /hospital-appointments/build/libs/*.jar hospital-appointments.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hospital-appointments.jar"]

FROM bellsoft/liberica-openjdk-alpine:26

LABEL authors="navaz"

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/awesome-0.0.1-SNAPSHOT.jar pizza-awesome.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "pizza-awesome.jar"]
FROM java:8-jdk-alpine
ARG JAR_FILE=target/rest-number-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} rest-number.jar
ENTRYPOINT ["java", "-jar", "/rest-number.jar"]
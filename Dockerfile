FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:21-jdk-slim
COPY --from=build /home/app/target/payment-processor-0.0.1-SNAPSHOT.jar /usr/local/lib/payment-processor-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/payment-processor-0.0.1-SNAPSHOT.jar"]

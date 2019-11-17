FROM openjdk:8-jdk-alpine

LABEL source="https://github.com/fiap-69aoj/netflix-gateway" \
      maintainer="flavioso16@gmail.com"

ADD ./target/gateway-0.0.1-SNAPSHOT.jar gateway.jar

EXPOSE 9091

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/gateway.jar"]
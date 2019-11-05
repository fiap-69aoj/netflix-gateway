FROM openjdk:8-jdk-alpine

LABEL source="https://github.com/fiap-69aoj/netflix-gateway" \
      maintainer="flavioso16@gmail.com"

ADD ./targer/gateway-0.0.1-SNAPSHOT.jar gateway.jar

EXPOSE 8091

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/gateway.jar"]
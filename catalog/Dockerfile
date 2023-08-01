FROM gradle:jdk17 AS build
WORKDIR /build
COPY . /build
RUN gradle catalog:build --no-daemon
RUN mkdir app
COPY --from=build "build/libs/*.jar" app/catalog.jar

#ENTRYPOINT ["java", "-jar", "/app/catalog.jar"]
FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=app/libs/*.jar
COPY ${JAR_FILE} catalog.jar
ENTRYPOINT ["java","-jar","app/catalog.jar"]
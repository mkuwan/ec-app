# temp container to build using gradle
FROM gradle:jdk17 AS build
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build


# actual container
FROM openjdk:17
ENV BUILD_ARTIFACT_NAME=admin-stock-0.0.1-SNAPSHOT.jar
ENV ARTIFACT_NAME=admin-stock.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/$BUILD_ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${BUILD_ARTIFACT_NAME}

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

#FROM gradle:jdk17 AS build
#WORKDIR /workspace
#COPY . /workspace
#RUN gradle build
#
#FROM openjdk:17 as publish
#VOLUME /tmp
#RUN mkdir /app
#EXPOSE 80
#
#ENV PORT=80
#ENV SPRING_PROFILES_ACTIVE=prod
#ENV DB_HOST=catalog-mysql
#ENV DB_PORT=3306
#ENV DB_NAME=catalog
#ENV DB_USER=mysql
#ENV DB_PASSWORD=P@ssword123
#ENV RABBIT_MQ_HOST=rabbitmq
#ENV RABBIT_MQ_PASSWORD=rabbitmq
#ENV RABBIT_MQ_USERNAME=rabbitmq
#
#COPY --from=build "/workspace/build/libs/*.jar" /app/admin-stock.jar
#
#ENTRYPOINT ["java", "-jar", "/app/admin-stock.jar"]
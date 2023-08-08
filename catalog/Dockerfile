FROM gradle:jdk17 AS build
WORKDIR /workspace
COPY . /workspace
RUN gradle build

#RUN mkdir /app

FROM openjdk:17 as publish
VOLUME /tmp
RUN mkdir /app
#
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
COPY --from=build "/workspace/build/libs/*.jar" /app/catalog.jar
#
ENTRYPOINT ["java", "-jar", "/app/catalog.jar"]

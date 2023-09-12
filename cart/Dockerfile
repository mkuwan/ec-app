# ビルド: -t イメージ名を指定する
# run: -d デーモン, -p 公開ポート:内部ポート --name (作成イメージ名) -it (使用するDocker image)
#   ビルドで --server.port=80とすれば指定できます(application.ymlの設定値を上書きする)
#   なしの場合はapplication.ymlのポートが使用されます

# docker build ./ -t cart
# docker run -d -p 3000:80 --name cart -it cart

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
ENV BUILD_ARTIFACT_NAME=cart-0.0.1-SNAPSHOT.jar
ENV ARTIFACT_NAME=cart.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/$BUILD_ARTIFACT_NAME .

#EXPOSE 80
ENTRYPOINT exec java -jar ${BUILD_ARTIFACT_NAME} --server.port=80
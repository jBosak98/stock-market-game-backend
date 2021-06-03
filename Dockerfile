FROM gradle:6.6.1-jdk11 AS BUILD_IMAGE
ENV APP_HOME=/app
WORKDIR $APP_HOME

COPY build.gradle  $APP_HOME
COPY settings.gradle $APP_HOME
COPY gradle.properties $APP_HOME
COPY resources $APP_HOME/resources
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src

USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

FROM openjdk:8-jre-alpine

ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY --from=BUILD_IMAGE $APP_HOME/build/libs/my-application.jar .

EXPOSE 8080
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "my-application.jar"]

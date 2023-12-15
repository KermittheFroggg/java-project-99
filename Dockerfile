#FROM node:20.6.1 AS frontend
#
#WORKDIR /frontend
#
#COPY /package*.json .
#
#RUN npm ci
#
#COPY . .
#
#RUN npm run build
#
#
#FROM gradle:8.1.1-jdk17 as builder
#
#WORKDIR /app
#
#COPY . .
#
#RUN gradle bootJar
#
#FROM openjdk:17-jdk
#
#WORKDIR /app
#
#ENV SPRING_PROFILES_ACTIVE=dev
#
#COPY --from=builder /app/build/libs/*.jar app.jar
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "app.jar"]

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.4

RUN apt-get update && apt-get install -yq unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR ./

COPY ./ .

RUN gradle build
ENV SPRING_PROFILES_ACTIVE=prod
CMD ["java", "-jar", "./build/libs/*.jar"]
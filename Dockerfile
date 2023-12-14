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
#ENV SPRING_PROFILES_ACTIVE=prod
#
#COPY --from=builder /app/build/libs/*.jar app.jar
#
#CMD ["java", "-jar", "app.jar"]

## Build the frontend
#FROM eclipse-temurin:20-jdk
#
#ARG GRADLE_VERSION=8.4
#
#RUN apt-get update && apt-get install -yq unzip
#
#RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
#    && unzip gradle-${GRADLE_VERSION}-bin.zip \
#    && rm gradle-${GRADLE_VERSION}-bin.zip
#
#ENV GRADLE_HOME=/opt/gradle
#
#RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}
#
#ENV PATH=$PATH:$GRADLE_HOME/bin
#
#WORKDIR .
#
#COPY . .
#
#RUN gradle bootJar
#
#ENV PORT=$PORT
#
#ENTRYPOINT ["java","-jar","/build/libs/app-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]

#FROM node:20.6.1 AS frontend
#
#WORKDIR /
#
#COPY package*.json .
#
#RUN npm ci
#
#COPY /frontend .
#
#RUN npm run build

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.3

RUN apt-get update && apt-get install -yq make unzip

WORKDIR /backend

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

RUN ./gradlew --no-daemon dependencies

COPY lombok.config .
COPY src src

COPY --from=frontend /frontend/dist /src/main/resources/static

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"
EXPOSE 8080

CMD java -jar build/libs/HexletSpringBlog-1.0-SNAPSHOT.jar
FROM node:20.6.1 AS frontend

WORKDIR /frontend

COPY /package*.json .

RUN npm ci

COPY . .

RUN npm run build


FROM gradle:8.1.1-jdk17 as builder

WORKDIR /app

COPY . .

RUN gradle bootJar

FROM openjdk:17-jdk

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=dev

COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]

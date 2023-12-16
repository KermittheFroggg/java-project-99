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
#ENV SPRING_PROFILES_ACTIVE=production
#
#COPY --from=builder /app/build/libs/*.jar app.jar
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "app.jar"]

FROM gradle:8.4-jdk20

WORKDIR ./

COPY ./ .

RUN gradle installDist

CMD ./build/install/app/bin/app --spring.profiles.active=production
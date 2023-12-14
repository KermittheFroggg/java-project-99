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
#

# Build the frontend
FROM node:14 as frontend
WORKDIR /
COPY . .
RUN npm install
RUN npm run build-frontend

# Build the backend
FROM gradle:8.1.1-jdk17 as builder
WORKDIR /
COPY . .
# Copy the built frontend files
COPY --from=frontend /src/main/resources/static /src/main/resources/static
RUN gradle bootJar

# Final image
FROM openjdk:17-jdk
WORKDIR /
ENV SPRING_PROFILES_ACTIVE=prod
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /src/main/resources/static /src/main/resources/static
CMD ["java", "-jar", "app.jar"]
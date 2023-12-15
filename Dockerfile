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
# Build the frontend
# Build the frontend
FROM node:14 as frontend
WORKDIR /app
COPY . .
RUN npm install --include=dev
RUN npm run build-frontend

# Build the backend
FROM gradle:8.1.1-jdk17 as builder
WORKDIR /app
COPY . .
# Copy the built frontend files
COPY --from=frontend /app/build /app/build
RUN gradle bootJar

# Final image
FROM openjdk:17-jdk
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/build /app/build
CMD ["java", "-jar", "app.jar"]
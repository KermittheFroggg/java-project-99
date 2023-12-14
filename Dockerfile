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

# Build the frontend
FROM node:14 as frontend
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Build the backend
FROM gradle:8.1.1-jdk17 as builder
WORKDIR /app
COPY . .
RUN gradle bootJar

# Final image
FROM openjdk:17-jdk
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
COPY --from=builder /app/build/libs/*.jar app.jar
# Copy built frontend files
COPY --from=frontend /app/build /app/static
CMD ["java", "-jar", "app.jar"]
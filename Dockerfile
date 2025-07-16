# Build React frontend
FROM node:20-alpine AS frontend-build
WORKDIR /frontend
COPY letter-web-app/package.json letter-web-app/package-lock.json* ./
RUN npm install
COPY letter-web-app ./
RUN npm run build

# Build Spring Boot backend, including frontend static files
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Copy frontend build into Spring Boot static resources
COPY --from=frontend-build /frontend/build ./src/main/resources/static
RUN mvn clean package -DskipTests

# Runtime image
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
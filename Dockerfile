# ── Stage 1: Build ───────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# Cache Maven dependency resolution separately from source compilation
COPY backend/pom.xml backend/pom.xml
RUN cd backend && mvn dependency:go-offline -B

# Copy backend source
COPY backend/src backend/src

# Bundle frontend static files into Spring Boot's static resource directory
# so the API and UI are served from the same origin (no CORS needed)
COPY *.html  backend/src/main/resources/static/
COPY *.css   backend/src/main/resources/static/
COPY *.js    backend/src/main/resources/static/
COPY imgs/   backend/src/main/resources/static/imgs/

# Build fat JAR
RUN cd backend && mvn clean package -DskipTests -B

# ── Stage 2: Runtime ─────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Data directory — mount a Render Disk here for H2 persistence
RUN mkdir -p /app/data

COPY --from=builder /build/backend/target/sors-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Dspring.h2.console.enabled=false", \
  "-Dspring.jpa.show-sql=false", \
  "-jar", "app.jar"]

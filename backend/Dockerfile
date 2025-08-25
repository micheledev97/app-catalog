# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# copia il pom e scarica le dipendenze (cache-friendly)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# copia i sorgenti e compila + repackage (fat jar Spring Boot)
COPY src ./src
RUN mvn -q -DskipTests package spring-boot:repackage

# ===== Runtime stage =====
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-Xms256m -Xmx512m"
WORKDIR /app

# se nel pom hai <finalName>catalogo-app</finalName>, il jar sarà catalogo-app.jar
COPY --from=build /app/target/catalogo-app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]

# ---------- Etapa 1: compila o projeto com Maven ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---------- Etapa 2: imagem final, só com o Java e o .jar ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/saferoute-api.jar app.jar
EXPOSE 8080
# Limite de memória pensado para o plano gratuito do Render (512 MB)
ENV JAVA_OPTS="-Xmx300m -Xss512k"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

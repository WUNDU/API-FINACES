
#FROM eclipse-temurin:17-jdk-alpine
#
#VOLUME /tmp
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#
#ENV SPRING_PROFILES_ACTIVE=prod
#
#ENTRYPOINT ["java","-jar","/app.jar"]

# Etapa 1: build
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw  # Dá permissões de execução ao mvnw
RUN ./mvnw clean package -DskipTests  # Executa o Maven

# Etapa 2: imagem final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
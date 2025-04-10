

## Etapa 1: build
#FROM eclipse-temurin:17-jdk-alpine as builder
FROM maven:3.9.9-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
#RUN chmod +x ./mvnw  # Dá permissões de execução ao mvnw
#RUN ./mvnw clean package -DskipTests  # Executa o Maven
RUN mvn clean package -DskipTests
#
## Etapa 2: imagem final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

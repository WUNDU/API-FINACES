# Use a imagem oficial do OpenJDK como base
FROM openjdk:17-jdk-slim

# Instalar o Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo pom.xml e baixe as dependências
COPY pom.xml /app/
RUN mvn dependency:go-offline -B

# Copie o código-fonte para o contêiner
COPY src /app/src

# Força a recompilação e limpa os caches do Maven
RUN mvn clean compile

# Compile o projeto e crie o arquivo JAR
RUN mvn clean package -U -DskipTests

# Exponha a porta que o aplicativo Spring Boot usará
EXPOSE 8080

# Comando para rodar o aplicativo
CMD ["java", "-jar", "target/api_finances-0.0.1-SNAPSHOT.jar"]
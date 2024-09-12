# Utiliser une image Maven pour la construction et une image OpenJDK pour l'exécution
FROM maven AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier POM et le code source
COPY pom.xml .
COPY src ./src
COPY .env ./

# Packager l'application avec Maven
# La commande mvn clean package exécute Maven pour construire l'application à partir du fichier pom.xml.
# L'option -DskipTests est utilisée pour ignorer l'exécution des tests pendant la construction de l'application.
RUN mvn clean package -DskipTests

# Utiliser une image OpenJDK pour l'exécution
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR généré par Maven dans l'image Docker
COPY --from=build /app/target/hebergement-0.0.1-SNAPSHOT.jar app.jar


# Exposer le port de l'application
EXPOSE 8082

# Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
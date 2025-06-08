# Usamos la imagen base de OpenJDK 21 (asegúrate de que esta imagen sea compatible con tu aplicación)
FROM eclipse-temurin:21-jdk-jammy

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR de la aplicación al contenedor
COPY target/finanSalud-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que Spring Boot ejecutará el servicio (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

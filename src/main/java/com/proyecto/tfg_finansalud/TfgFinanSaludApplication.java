package com.proyecto.tfg_finansalud;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TfgFinanSaludApplication {

    public static void main(String[] args) {
        // Cargar desde variables de entorno del sistema si existen
        setSystemPropertyIfPresent("EMAIL_USERNAME");
        setSystemPropertyIfPresent("EMAIL_PASSWORD");
        setSystemPropertyIfPresent("MONGO_URI");

        // Si alguna variable crítica no está definida, intenta cargar desde .env
        if (System.getProperty("EMAIL_USERNAME") == null ||
                System.getProperty("EMAIL_PASSWORD") == null ||
                System.getProperty("MONGO_URI") == null) {
            try {
                Dotenv dotenv = Dotenv.configure()
                        .directory(".")
                        .filename(".env")
                        .load();

                dotenv.entries().forEach(entry ->
                        System.setProperty(entry.getKey(), entry.getValue())
                );
                System.out.println("📄 Variables cargadas desde archivo .env");
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo cargar el archivo .env: " + e.getMessage());
            }
        } else {
            System.out.println("✅ Variables cargadas desde el entorno");
        }

        SpringApplication.run(TfgFinanSaludApplication.class, args);
    }

    private static void setSystemPropertyIfPresent(String key) {
        String value = System.getenv(key);
        if (value != null) {
            System.setProperty(key, value);
        }
    }

}

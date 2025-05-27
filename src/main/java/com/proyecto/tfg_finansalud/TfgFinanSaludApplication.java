package com.proyecto.tfg_finansalud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TfgFinanSaludApplication {

    public static void main(String[] args) {
        SpringApplication.run(TfgFinanSaludApplication.class, args);
    }

}

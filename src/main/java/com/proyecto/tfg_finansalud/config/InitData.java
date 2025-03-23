package com.proyecto.tfg_finansalud.config;


import com.proyecto.tfg_finansalud.entities.Profile;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.services.ProfileService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Configuration
@Component
@RequiredArgsConstructor
public class InitData {

    final ProfileService profileService;
    final UserService userService;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initData();
    }
    private void initData() {
        Usuario usuario = Usuario.builder().username("pao").email("pao@pao.com").password("pao").registrationDate(LocalDate.now()).build();

        Profile profile = Profile.builder().name("admin").build();

        profileService.save(profile);
        try {
            userService.save(usuario);
        }catch (Exception e){
            System.out.println("Error al registrar el usuario");
        }

    }
}

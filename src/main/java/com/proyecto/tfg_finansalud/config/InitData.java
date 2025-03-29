package com.proyecto.tfg_finansalud.config;


import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Profile;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.ProfileService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Configuration
@Component
@RequiredArgsConstructor
public class InitData {

    final ProfileService profileService;
    final UserService userService;
    final BudgetService budgetService;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initData();
    }
    private void initData() {
        Profile profile = Profile.builder().name("admin").build();
        Set<Profile> profiles = new HashSet<>();
        profiles.add(profile);
        Usuario usuario = Usuario.builder().username("pao").email("pao@pao.com").password("pao").registrationDate(LocalDate.now()).profiles(profiles).build();

        List<Budget> budgets = Arrays.asList(
                Budget.builder().name("entretenimiento").budget(400.0).budgetCount(200.1).color("red").build(),
                Budget.builder().name("alimentación").budget(300.0).budgetCount(250.60).color("blue").build()
        );
        usuario.setBudgets(budgets);
        //budgetService.saveAll(budgets);



        profileService.save(profile);
        try {
            userService.save(usuario);
        }catch (Exception e){
            System.out.println("Error al registrar el usuario");
        }

    }
}

package com.proyecto.tfg_finansalud.config;

import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@RequiredArgsConstructor
@Configuration
public class AuthConfig {
    final UserRepository userRepository;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCryptPasswordEncoder para el encriptado de contraseñas
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthPro = new DaoAuthenticationProvider();
        daoAuthPro.setUserDetailsService(userDetailsService());
        daoAuthPro.setPasswordEncoder(passwordEncoder());
        return daoAuthPro;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final Usuario user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        };
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
package com.proyecto.tfg_finansalud.config;

import com.proyecto.tfg_finansalud.repositories.TokenRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import com.proyecto.tfg_finansalud.security.jwt.JwtAuthFilter;
import com.proyecto.tfg_finansalud.security.jwt.JwtService;
import com.proyecto.tfg_finansalud.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

// AppConfig.java (o JwtConfig.java)
@Configuration
public class JwtConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            TokenRepository tokenRepository,
            UserRepository userRepository
    ) {
        return new JwtAuthFilter(
                jwtService,
                userDetailsService,
                tokenRepository,
                userRepository
        );
    }
}

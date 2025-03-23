package com.proyecto.tfg_finansalud.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Value("${http.recuerdameKey}") final String recuerdameKey) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF si usas formularios tradicionales. Para APIs REST sería importante habilitar CSRF.
                .authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/api/login","/register").permitAll()
                        .anyRequest().authenticated()
                        )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // URL de la página de login
                                .loginProcessingUrl("/api/login") // URL donde se procesan las solicitudes de loginnnn
                                .permitAll() // Permitir el acceso al login sin autenticación
                                .defaultSuccessUrl("/dashboard", true)
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .permitAll()
                )
                .build();
//        return http
//                .csrf(csrf -> csrf.disable()) // Desactivar CSRF (si es necesario)
//
//                .formLogin(form -> form
//                        .loginPage("/login") // Página de login (la página que se mostrará al usuario)
//                        .loginProcessingUrl("/api/login") // URL a la que se enviará el formulario de login
//                        .defaultSuccessUrl("/dashboard", true) // Página de destino después de un login exitoso
//                        .permitAll() // Permitir acceso sin autenticación al login
//                )
//                .rememberMe(rememberMe -> rememberMe
//                        .rememberMeParameter("recuerdame") // Parámetro para el checkbox "Recordarme"
//                        .rememberMeCookieName("yo-soy-aquel") // Nombre de la cookie
//                        .tokenValiditySeconds((int) Duration.ofDays(180).getSeconds()) // Validez del token
//                        .key(recuerdameKey)) // Clave para las cookies
//                .logout(out -> out
//                        .logoutUrl("/logout") // URL para logout
//                        .logoutSuccessUrl("/usuario/login?logout") // URL de redirección después de logout
//                        .permitAll() // Permitir logout sin autenticación
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/register", "/home", "/api/login").permitAll() // Permitir ciertas rutas sin autenticación
//                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
//                )
//                .httpBasic(withDefaults()) // Autenticación básica (si la necesitas)
//                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCryptPasswordEncoder para el encriptado de contraseñas
    }
}

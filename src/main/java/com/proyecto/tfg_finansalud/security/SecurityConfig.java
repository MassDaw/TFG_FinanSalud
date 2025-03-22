package com.proyecto.tfg_finansalud.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http,
                                                    @Value("${http.recuerdameKey}") final String recuerdameKey) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/usuario/**", "/webjars/**","/css/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/usuario/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll())
//                .oauth2Login(oauth -> oauth
//                        .loginPage("/usuario/login")
//                        .defaultSuccessUrl("/index", true)
//                        .userInfoEndpoint(endpoint -> endpoint.userService(oauth2UserService)))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("recuerdame") // nombre del checkbox del formulario
                        .rememberMeCookieName("yo-soy-aquel")
                        .tokenValiditySeconds((int) Duration.ofDays(180).getSeconds()) // validez del token
                        .key(recuerdameKey)) // cookies will survive if restarted
                .logout(out -> out
                        .logoutUrl("/usuario/logout")
                        .logoutSuccessUrl("/usuario/login?logout").permitAll());

        return http.build();

    }
}

package com.proyecto.tfg_finansalud.security;

import com.proyecto.tfg_finansalud.entities.Token;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.TokenRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import com.proyecto.tfg_finansalud.security.jwt.JwtAuthFilter;
import com.proyecto.tfg_finansalud.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    final UserRepository userRepository;
    final AuthenticationProvider authenticationProvider;
    @Lazy
    final JwtAuthFilter jwt;
    final TokenRepository tokenRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Value("${http.recuerdameKey}") final String recuerdameKey) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)// Desactiva CSRF si usas formularios tradicionales. Para APIs REST sería importante habilitar CSRF.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/api/login",
                                "/register", "/css/**" ,"/js/**" ,"/api/register", "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login") // URL de la página de login
//                                .loginProcessingUrl("/api/login") // URL donde se procesan las solicitudes de loginnnn
//                                .permitAll() // Permitir el acceso al login sin autenticación
//                                .defaultSuccessUrl("/dashboard", true)
//                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .addLogoutHandler((request, response, authentication) -> {
                                    final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                                    logout(authHeader);
                                })
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    SecurityContextHolder.clearContext();
                                })
                )
                .build();


    }
    private void logout(String token) {
        if(token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Token no proporcionado");
        }
        final String jwt = token.substring(7);
        final Token foundToken = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new RuntimeException("Token no encontrado"));
        foundToken.setRevoked(true);
        foundToken.setExpired(true);
        tokenRepository.save(foundToken);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "https://finan-salud-front.vercel.app",
                "http://localhost:3000",
                "http://127.0.0.1:5500",
                "https://localhost:3000"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
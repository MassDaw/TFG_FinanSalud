package com.proyecto.tfg_finansalud.security.jwt;

import com.proyecto.tfg_finansalud.entities.Token;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.TokenRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import com.proyecto.tfg_finansalud.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/auth") || request.getServletPath().contains("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userUsername = jwtService.extractUsername(jwt);
        if (userUsername == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        Token token = null;
        try {
            token = tokenRepository.findByToken(jwt).get();

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("jwtAuthFilter:  Error al obtener el token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        if (token.isExpired() || token.isRevoked() || token == null) {
            filterChain.doFilter(request, response);
            return;
        };
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userUsername);
        Optional<Usuario> user = userRepository.findByUsername(userUsername);

        if (user.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
            return;
        }
        final boolean isTokenValid = jwtService.isTokenValid(jwt, user.get());
        if (!isTokenValid) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        final var authToken = new UsernamePasswordAuthenticationToken
                (userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}

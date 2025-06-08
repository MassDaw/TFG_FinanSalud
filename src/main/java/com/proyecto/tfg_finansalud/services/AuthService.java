package com.proyecto.tfg_finansalud.services;


import com.proyecto.tfg_finansalud.DTO.jwt.LoginRequest;
import com.proyecto.tfg_finansalud.DTO.jwt.RegisterRequest;
import com.proyecto.tfg_finansalud.DTO.jwt.TokenResponse;
import com.proyecto.tfg_finansalud.entities.Token;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.TokenRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import com.proyecto.tfg_finansalud.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public TokenResponse register(RegisterRequest registerRequest){
        Usuario user = userService.save(Usuario.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .build());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return TokenResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return TokenResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Usuario user, String jwtToken) {
        var token = Token.builder()
                .usuario(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false).build();
        tokenRepository.save(token);
    }

    public TokenResponse refreshToken(String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no proporcionado");
        }


        final String refreshtoken = authHeader.substring("Bearer ".length());
        final String username = jwtService.extractUsername(refreshtoken);
        if (username == null) {
            throw new RuntimeException("Token inválido o expirado");
        }
        final Usuario usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!jwtService.isTokenValid(refreshtoken, usuario)) {
            throw new RuntimeException("invalid refresh token");
        }
        final String accessToken = jwtService.generateToken(usuario);
        saveUserToken(usuario, accessToken);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshtoken)
                .build();

    }

}

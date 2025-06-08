package com.proyecto.tfg_finansalud.security.jwt;

import com.proyecto.tfg_finansalud.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
@Component
@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token-expiration-time}")
    private long jwtRefreshExpiration;

    public String generateToken(final Usuario user) {
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(final Usuario user) {
        return buildToken(user, jwtRefreshExpiration);
    }

    private String buildToken(final Usuario user, final long expiration) {
        return Jwts.builder()
                .id(user.getId())
                .claims(Map.of("email", user.getEmail()))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSingKey())
                .compact();
    }

    private SecretKey getSingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(final String token) {
        final Claims jwtClaims = Jwts.parser()
                .setSigningKey(getSingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return jwtClaims.getSubject();
    }



    public boolean isTokenValid(final String token, final Usuario user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(final String token) {
        final Claims jwtClaims = Jwts.parser()
                .setSigningKey(getSingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return jwtClaims.getExpiration();
    }

}

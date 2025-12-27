package com.projectManagement.project.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import com.projectManagement.project.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final byte[] jwtSecret;
    private final long jwtExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        this.jwtSecret = secret.getBytes(StandardCharsets.UTF_8);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(Authentication authentication, UserEntity user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .claim("email",authentication.getName())
                .claim("userName", user.getName())
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret))
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = parseClaims(token);
        return (String)claims.get("email");
    }

    public boolean validateToken(String authToken) {
        parseClaims(authToken);
        return true;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


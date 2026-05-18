package com.uep.sors.security;

import com.uep.sors.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "uep-sors-super-secret-key-2024-spring";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getStudentId())
            .claim("role", user.getRole().name())
            .claim("username", user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(key)
            .compact();
    }

    public String extractStudentId(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key).build()
            .parseClaimsJws(token)
            .getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
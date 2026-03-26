package com.example.demo.config;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private String SECRET = "mySuperSecretKeyThatIsAtLeast32CharactersLong!!";
    private Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔹 Extract all claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔹 Extract username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 🔹 Extract role
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 🔹 Validate token
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}

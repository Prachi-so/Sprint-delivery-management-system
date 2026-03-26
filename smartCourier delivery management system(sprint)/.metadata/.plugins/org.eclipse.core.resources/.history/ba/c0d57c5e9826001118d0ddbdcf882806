package com.lpu.auth_service.config;

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
	private Key key = Keys.hmacShaKeyFor(SECRET.getBytes()); //HMACSHA  + HS256

    // Generate Token  /login
    public String generateToken(String userName,String role) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour(ms,s,min)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // Extract Username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }
    
    //extract role
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Validate Token  -> profile
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }
    //pay load -> username, created time, expiery time, role
    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}

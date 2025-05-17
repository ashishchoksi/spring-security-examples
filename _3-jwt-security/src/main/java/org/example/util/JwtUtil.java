package org.example.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String secret = "your-secure-secret-key-min-32-bytes";
    private static final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String name, long expiryMinutes) {

        return Jwts.builder()
                .setSubject(name)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (expiryMinutes * 60 * 1000))) // in ms
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public String extratUserName(String token) {
        try {
            return Jwts.parser().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {return null;}
    }
}

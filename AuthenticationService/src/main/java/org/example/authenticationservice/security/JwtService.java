package org.example.authenticationservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final Key key;
    private final long accessTtlMillis;

    public JwtService(
            @Value("${application.secret}") String secret,
            @Value("${jwt.expiration}") long expMillis
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlMillis = expMillis * 60_000L;
    }

    public String generateAccess(String username, String role, Long id) {
        var now = Instant.now();
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("id", id)
                .setExpiration(Date.from(now.plusMillis(accessTtlMillis)))
                .signWith(key)
                .compact();
    }

    public String validateAndGetSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {
        var body = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        Object r = body.get("role");
        return r == null ? null : r.toString();
    }
}

package org.example.authenticationservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final Key key;
    private final long accessTtlMillis;
    private final long refreshTtlMillis;

    public JwtService(
            @Value("${application.secret}") String secret,
            @Value("${jwt.expiration}") long expMillis
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTtlMillis = expMillis * 60_000L;
        this.refreshTtlMillis = 7 * 24L * 60L * 60L * 1000L;
    }

    public String generateAccess(String username, String role) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setClaims(Map.of("role", role))
                .setIssuedAt(Date.from(now))
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

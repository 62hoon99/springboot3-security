package org.example.springboot3security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.springboot3security.member.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenManager {

    private final Key secretKey = Keys.hmacShaKeyFor(
            "your-super-secure-secret-key-that-is-at-least-32-bytes".getBytes()
    );

    public String createRefreshToken(Member member) {
        return createToken(member, 180);
    }

    public String createAccessToken(Member member) {
        return createToken(member, 30);
    }

    private String createToken(Member member, long expirationSeconds) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expirationSeconds * 1000); // 초 → 밀리초

        return Jwts.builder()
                .setSubject(member.getUsername())
                .claim("authority", "USER")
                .claim("username", member.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String username = claims.get("username", String.class);
        String authority = claims.get("authority", String.class);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(authority))
        );
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

}

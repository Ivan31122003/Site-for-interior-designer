package com.malina.siteForInteriorDesigner.Data;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

public class Token {
    @Getter
    private final String token;
    public Token(String token) {
        this.token = token;
    }
    public static Token of(String token) {
        return new Token(token);
    }
    public static Token of(Long userId, Long validityInMinutes, String secretKey) {
        var issueDate = Instant.now();
        return new Token(
                Jwts.builder()
                    .claim("user_id", userId)
                    .setIssuedAt(Date.from(issueDate))
                    .setExpiration(Date.from(issueDate.plus(validityInMinutes, ChronoUnit.MINUTES)))
                    .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .compact()
        );
    }
    public static Long from(String token, String secretKey) {
        return ((Claims) Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parse(token)
                .getBody())
                .get("user_id", Long.class);
    }
}

package com.marlonb.game_leaderboard_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final String secretKey;
    private final long expirationDate;

    public JWTService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.expiration-ms}") long expirationDate) {
        this.secretKey = secretKey;
        this.expirationDate = expirationDate;
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* --------- Token generation --------- */
    public String generateToken(String username) {
        return generateToken(Map.of(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationDate))
                .and()
                .signWith(getKey())
                .compact();
    }

    /* --------- Token validation --------- */
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /* --------- Claim extraction --------- */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}

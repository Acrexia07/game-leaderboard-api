package com.marlonb.game_leaderboard_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    // Decode the Base64-encoded secret key and convert it into an HMAC-SHA SecretKey
    // for signing and verifying JWT tokens.
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* --------- Token generation --------- */
    public String generateToken(String username) {
        return generateToken(Map.of(), username);
    }

    // Generates a signed JWT token for the given username,
    // including any additional claims, issued-at time, and expiration.
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
    // Validates the JWT by checking if the username matches
    // and the token has not expired.
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Checks if the JWT token has already expired.
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /* --------- Claim extraction --------- */
    // Extracts the username (subject) from the JWT token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Generic method to extract a specific claim from the JWT token
    // using a claims resolver function.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse the JWT token using the signing key, verify its signature,
    // and extract all claims (payload) from the token.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}

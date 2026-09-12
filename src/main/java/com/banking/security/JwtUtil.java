// src/main/java/com/banking/security/JwtUtil.java
package com.banking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // Loaded from application.properties (app.jwtSecret / app.jwtExpirationInMs).
    // IMPORTANT: app.jwtSecret must be at least 32 characters (256 bits) for HS256,
    // or key generation throws WeakKeyException at startup. Generate a real one with:
    //   openssl rand -base64 32
    private final SecretKey secretKey;
    private final long jwtExpirationMs;

    public JwtUtil(@Value("${app.jwtSecret}") String jwtSecret,
                    @Value("${app.jwtExpirationInMs}") long jwtExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /** Generates a token carrying username as subject and role as a claim. */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    /** Overload for callers that don't need a role claim embedded. */
    public String generateToken(String username) {
        return createToken(new HashMap<>(), username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

   private Claims extractAllClaims(String token) {
	   return Jwts.parserBuilder()
			   .setSigningKey(secretKey)
			   .build()
			   .parseClaimsJws(token)
			   .getBody();
			   
   }

    /**
     * Validates signature, expiry, and subject match in one pass.
     * Never throws — returns false for any malformed/expired/tampered token,
     * so callers (the filter) can fail closed without crashing the request.
     */
    public boolean validateToken(String token, String expectedUsername) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject().equals(expectedUsername) && !isTokenExpired(claims);
        } catch (ExpiredJwtException e) {
            logger.debug("JWT expired: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT encountered: {}", e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
package com.banking.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final int JWT_EXPIRATION = 86400000;
	
	 public String generateToken(String username) {
	        Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, username);
	 }

	 private String createToken(Map<String, Object> claims, String subject) {
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(subject)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
	                .signWith(SECRET_KEY)
	                .compact();
	 }
	 
	 public Boolean validateToken(String token, String username) {
	        final String extractedUsername = extractUsername(token);
	        return (extractedUsername.equals(username) && !isTokenExpired(token));
	 }
	 
	 public String extractUsername(String token) {
	        return extractAllClaims(token).getSubject();
	 }

	 public Date extractExpiration(String token) {
	        return extractAllClaims(token).getExpiration();
	 }
	 
	 private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
	 }

	 private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	 }
}

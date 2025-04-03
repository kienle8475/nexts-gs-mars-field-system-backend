package com.nexts.gs.mars.nexts_gs_mars_field_service.utils;

import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  public String generateToken(User user) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key,
            SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    Date expiration = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
    return expiration.before(new Date());
  }
}

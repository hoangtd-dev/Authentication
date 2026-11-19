package com.backend10.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
	private final SecretKey key;
	private final long expirationMs;

	public JwtUtil(String secret, long expirationMs) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	public String generateToken(String username) {
		Date expiredAt = new Date(System.currentTimeMillis() + expirationMs);
		return Jwts.builder()
				.subject(username)
				.expiration(expiredAt)
				.signWith(key)
				.compact();
	}

	public boolean verifyToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

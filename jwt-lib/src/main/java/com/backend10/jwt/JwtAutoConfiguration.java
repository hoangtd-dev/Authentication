package com.backend10.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class JwtAutoConfiguration {

	@Bean
	public AuthenticationAspect initAuthenticationAspect(JwtUtil jwtUtil) {
		return new AuthenticationAspect(jwtUtil);
	}

	@Bean
	public JwtUtil jwtUtil(@Value("${JWT_SECRET}") String secret,
			@Value("${JWT_EXPIRATION_MS:3600000}") long expirationMs) {
		return new JwtUtil(secret, expirationMs);
	}
}

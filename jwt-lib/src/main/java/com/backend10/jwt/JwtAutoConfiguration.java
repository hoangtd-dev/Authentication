package com.backend10.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
public class JwtAutoConfiguration {

	@Bean
	public JwtUtil jwtUtil(@Value("${JWT_SECRET}") String secret,
			@Value("${JWT_EXPIRATION_MS:3600000}") long expirationMs) {
		return new JwtUtil(secret, expirationMs);
	}

	@Bean
	public WebMvcConfigurer jwtWebMvcConfigurer(JwtUtil jwtUtil) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(new JwtInterceptor(jwtUtil));
			}
		};
	}
}

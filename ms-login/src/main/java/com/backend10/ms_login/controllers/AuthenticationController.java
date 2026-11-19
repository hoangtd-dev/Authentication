package com.backend10.ms_login.controllers;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend10.jwt.JwtUtil;
import com.backend10.ms_login.entities.User;
import com.backend10.ms_login.models.AuthParams;
import com.backend10.ms_login.models.LoginResponse;
import com.backend10.ms_login.models.RefreshParams;
import com.backend10.ms_login.models.RefreshResponse;
import com.backend10.ms_login.repositories.AuthenticationRepository;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	private final AuthenticationRepository authRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final StringRedisTemplate redis;
	private final long refreshTtlSeconds;

	public AuthenticationController(AuthenticationRepository authRepository, PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil, StringRedisTemplate redis,
			@Value("${REFRESH_TOKEN_TTL_SECONDS}") long refreshTtlSeconds) {
		this.authRepository = authRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.redis = redis;
		this.refreshTtlSeconds = refreshTtlSeconds;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody AuthParams params) {
		boolean usernameTaken = authRepository.findByUsername(params.username()).isPresent();
		if (usernameTaken) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
		}

		String passwordHash = passwordEncoder.encode(params.password());
		authRepository.save(new User(params.username(), passwordHash));
		return ResponseEntity.status(HttpStatus.CREATED).body("Registered");
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody AuthParams params) {
		User user = authRepository.findByUsername(params.username()).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		boolean passwordCorrect = passwordEncoder.matches(params.password(), user.getPasswordHash());
		if (!passwordCorrect) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String accessToken = jwtUtil.generateToken(user.getUsername());
		String refreshToken = createRefreshToken(user.getUsername());
		return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
	}

	@PostMapping("/refresh")
	public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshParams params) {
		if (params.refreshToken() == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String username = redis.opsForValue().get("refresh:" + params.refreshToken());
		if (username == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String accessToken = jwtUtil.generateToken(username);
		return ResponseEntity.ok(new RefreshResponse(accessToken));
	}

	private String createRefreshToken(String username) {
		String refreshToken = UUID.randomUUID().toString();
		Duration timeToLive = Duration.ofSeconds(refreshTtlSeconds);
		redis.opsForValue().set("refresh:" + refreshToken, username, timeToLive);
		return refreshToken;
	}
}

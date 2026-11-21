package com.backend10.jwt;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
public class AuthenticationAspect {
	private final JwtUtil jwtUtil;
	private static final Logger log = LoggerFactory.getLogger(AuthenticationAspect.class);

	public AuthenticationAspect(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Around("@annotation(com.backend10.jwt.RequireJwt)")
	public Object handleAuthentication(ProceedingJoinPoint pjp) throws Throwable {
		log.info("RequireJwt triggered !!!");
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();

			String header = request.getHeader("Authorization");
			if (header == null || !header.startsWith("Bearer ")) {
				log.error("Missing token: ", header);
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
			}

			String token = header.substring("Bearer ".length());
			if (!jwtUtil.verifyToken(token)) {
				log.error("Invalid or expired token");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
			}
		}

		return pjp.proceed();
	}
}

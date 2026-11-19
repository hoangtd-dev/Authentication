package com.backend10.jwt;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
	private final JwtUtil jwtUtil;

	public JwtInterceptor(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod method = (HandlerMethod) handler;

		boolean onMethod = method.hasMethodAnnotation(RequireJwt.class);
		boolean onClass = method.getBeanType().isAnnotationPresent(RequireJwt.class);
		if (!onMethod && !onClass) {
			return true;
		}

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
			return false;
		}

		String token = header.substring("Bearer ".length());
		if (!jwtUtil.verifyToken(token)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
			return false;
		}

		return true;
	}
}

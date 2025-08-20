package org.example.loginapi.jwt;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.common.exception.CustomException;
import org.example.loginapi.common.exception.ErrorCode;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	// 로그인 필터 제외 대상 URL
	private static final List<String> WHITE_LIST = List.of(
		"/signup"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String url = request.getRequestURI();

		// WHITE_LIST 는 필터 적용 제외
		for (String matcher : WHITE_LIST) {
			if (url.startsWith(matcher)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		String bearerToken = request.getHeader("Authorization");

		// 토큰이 비어있다면
		if (bearerToken == null) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}

		// 유효한 토큰인지 검사
		String token = jwtUtil.substringToken(bearerToken);
		Claims claims = null;

		try {
			claims = jwtUtil.extractClaims(token);

			// 유저 역할 검증 기능 ---> 권한 없음 예외 발생

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException | SignatureException | MalformedJwtException |
				 UnsupportedJwtException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}
}

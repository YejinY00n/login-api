package org.example.loginapi.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.loginapi.auth.enums.UserRole;
import org.example.loginapi.common.exception.ErrorCode;
import org.example.loginapi.common.exception.ErrorResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	// 로그인 필터 제외 대상 URL
	private static final List<String> WHITE_LIST = List.of(
		"/signup",
		"/login"
	);

	// Admin 권한 URL
	private static final List<String> ADMIN_LIST = List.of(
		"/admin/**"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String url = request.getRequestURI();

		// WHITE_LIST 는 필터 적용 제외
		for (String matcher : WHITE_LIST) {
			if (pathMatcher.match(matcher, url)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		String bearerToken = request.getHeader("Authorization");

		// 토큰이 비어있다면
		if (bearerToken == null) {
			filterErrorResponse(response, ErrorCode.INVALID_TOKEN);
		}

		// 유효한 토큰인지 검사
		String token = jwtUtil.substringToken(bearerToken);
		Claims claims = null;

		try {
			claims = jwtUtil.extractClaims(token);

			String username = claims.getSubject();
			UserRole role = UserRole.valueOf(claims.get("roles", String.class));

			// Auth 객체 생성
			UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(
					username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
			SecurityContextHolder.getContext().setAuthentication(auth);

			// Admin 권한 경로에 대해 접근 권한 검증
			for (String matcher : ADMIN_LIST) {
				if (pathMatcher.match(matcher, url)) {

					// 만약 유저 role 이 일반 사용자(USER)라면
					if (!UserRole.ADMIN.equals(role)) {
						filterErrorResponse(response, ErrorCode.ACCESS_DENIED);
					}
				}
			}

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException | SignatureException | MalformedJwtException |
				 UnsupportedJwtException | IllegalArgumentException e) {
			filterErrorResponse(response, ErrorCode.INVALID_TOKEN);
		}
	}

	private void filterErrorResponse(HttpServletResponse response, ErrorCode jwtErrorCode) {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(jwtErrorCode.getStatus().value());

		ErrorResponse error = new ErrorResponse(jwtErrorCode);

		try (PrintWriter writer = response.getWriter()) {
			new ObjectMapper().writeValue(writer, error);
		} catch (IOException e) {
			log.error("JwtFilter 예외 쓰기 실패: {}", e.getMessage());
		}
	}

}

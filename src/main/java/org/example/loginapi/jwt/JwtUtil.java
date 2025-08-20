package org.example.loginapi.jwt;

import static org.example.loginapi.common.exception.ErrorCode.INVALID_TOKEN;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.enums.UserRole;
import org.example.loginapi.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
public class JwtUtil {
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;    // 비밀키(secretKey)를 통해 키 생성
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;        // 비밀키 알고리즘

	@Value("${jwt.token.expiration}")
	private Long TOKEN_TIME;    // 토큰 유효시간, 60분
	public static final String BEARER_PREFIX = "Bearer ";        // Token 식별자

	// 스프링 컨테이너 초기화 이후 1회 초기화 실행
	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	// 토큰 발급
	public String createToken(Long userId, String email, UserRole role) {
		// 페이로드
		Date now = new Date();
		Date exp = new Date(now.getTime() + TOKEN_TIME);

		return Jwts.builder()
			.setSubject(String.valueOf(userId))                       // 토큰의 주체
			.setHeaderParam("typ", "JWT")                       // 헤더 설정
			.setIssuedAt(now)                                       // 발행 시간
			.setExpiration(exp)                             // 토큰 만료기한 (발급 일시 + TOKEN_TIME)
			.claim("roles", role)                        // Private Claims (Key-Value)
			.signWith(key, signatureAlgorithm)            // 서명 (사용 알고리즘, 서명 생성-검증 용 비밀 키)
			.compact();
	}

	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		throw new CustomException(INVALID_TOKEN);
	}

	// 토큰 바디(Claims) 반환
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(token).getBody();
	}
}


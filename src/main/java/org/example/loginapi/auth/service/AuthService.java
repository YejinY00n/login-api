package org.example.loginapi.auth.service;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.dto.LoginRequestDto;
import org.example.loginapi.auth.dto.LoginResponseDto;
import org.example.loginapi.auth.dto.SignupRequestDto;
import org.example.loginapi.auth.dto.SignupResponseDto;
import org.example.loginapi.auth.entity.User;
import org.example.loginapi.auth.repository.UserRepository;
import org.example.loginapi.common.exception.CustomException;
import org.example.loginapi.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;

	// 회원 가입
	public SignupResponseDto signup(SignupRequestDto requestDto) {
		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
		}

		User user = userRepository.save(new User(requestDto));
		return new SignupResponseDto(user);
	}

	// 로그인
	public LoginResponseDto login(LoginRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername())
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

		// 패스워드 검증 --> 예외 던지기

		// 토큰 생성
		return new LoginResponseDto("token");
	}
}

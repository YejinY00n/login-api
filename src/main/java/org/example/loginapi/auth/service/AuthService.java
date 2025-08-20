package org.example.loginapi.auth.service;

import lombok.RequiredArgsConstructor;

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
	private final UserRepository authRepository;

	// 회원 가입
	public SignupResponseDto signup(SignupRequestDto requestDto) {
		if (authRepository.existsByUsername(requestDto.getUsername())) {
			throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
		}

		User user = authRepository.save(new User(requestDto));
		return new SignupResponseDto(user);
	}

}

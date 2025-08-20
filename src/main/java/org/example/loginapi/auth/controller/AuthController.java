package org.example.loginapi.auth.controller;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.dto.SignupRequestDto;
import org.example.loginapi.auth.dto.SignupResponseDto;
import org.example.loginapi.auth.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@RequestMapping("/signup")
	@PostMapping
	public SignupResponseDto signup(@RequestBody SignupRequestDto requestDto) {
		return authService.signup(requestDto);
	}
}

package org.example.loginapi.auth.controller;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.dto.LoginRequestDto;
import org.example.loginapi.auth.dto.LoginResponseDto;
import org.example.loginapi.auth.dto.SignupRequestDto;
import org.example.loginapi.auth.dto.SignupResponseDto;
import org.example.loginapi.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@RequestMapping("/signup")
	@PostMapping
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
		return ResponseEntity.ok(authService.signup(requestDto));
	}

	@RequestMapping("/login")
	@PatchMapping
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
		return ResponseEntity.ok(authService.login(requestDto));
	}
}

package org.example.loginapi.auth.controller;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.dto.LoginRequestDto;
import org.example.loginapi.auth.dto.LoginResponseDto;
import org.example.loginapi.auth.dto.SignupRequestDto;
import org.example.loginapi.auth.dto.SignupResponseDto;
import org.example.loginapi.auth.service.AuthService;
import org.example.loginapi.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "회원가입 api", description = "새로 회원가입 요청 api")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "중복 회원가입",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
		return ResponseEntity.ok(authService.signup(requestDto));
	}

	@Operation(summary = "로그인 api", description = "가입한 계정으로 로그인")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "400", description = "아이디/비밀번호 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PatchMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
		return ResponseEntity.ok(authService.login(requestDto));
	}
}

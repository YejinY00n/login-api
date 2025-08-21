package org.example.loginapi.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.loginapi.auth.dto.LoginRequestDto;
import org.example.loginapi.auth.dto.SignupRequestDto;
import org.example.loginapi.auth.entity.User;
import org.example.loginapi.auth.enums.UserRole;
import org.example.loginapi.auth.repository.UserRepository;
import org.example.loginapi.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ObjectMapper objectMapper;

	private static final String USERNAME = "test1234";
	private static final String PASSWORD = "test1234";
	private static final String NICKNAME = "nickname";

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Nested
	@DisplayName("회원가입 API 테스트")
	class SignupTest {
		@Test
		@DisplayName("회원가입 성공")
		void successSignup() throws Exception {
			// given
			SignupRequestDto requestDto = new SignupRequestDto(USERNAME, PASSWORD, NICKNAME);

			// when & then
			mockMvc.perform(post("/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value(USERNAME))
				.andExpect(jsonPath("$.nickname").value(NICKNAME))
				.andExpect(jsonPath("$.roles[0]").value(UserRole.USER.toString()));
		}

		@Test
		@DisplayName("회원가입 실패 - 중복 유저")
		void duplicatedSignup() throws Exception {
			// given
			SignupRequestDto requestDto = new SignupRequestDto(USERNAME, PASSWORD, NICKNAME);
			userRepository.save(new User(requestDto));

			// when & then
			mockMvc.perform(post("/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value(ErrorCode.USER_ALREADY_EXISTS.getCode()))
				.andExpect(jsonPath("$.error.message").value(ErrorCode.USER_ALREADY_EXISTS.getMessage()));
		}
	}

	@Nested
	@DisplayName("로그인 API 테스트")
	class LoginTest {
		@Test
		@DisplayName("로그인 성공")
		void successLogin() throws Exception {
			// given
			SignupRequestDto requestDto = new SignupRequestDto(USERNAME, PASSWORD, NICKNAME);
			userRepository.save(new User(requestDto));

			LoginRequestDto loginRequestDto = new LoginRequestDto(USERNAME, PASSWORD);

			// when & then
			mockMvc.perform(patch("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isString());
		}

		@Test
		@DisplayName("로그인 실패 - 틀린 비밀번호")
		void loginWithWrongPassword() throws Exception {
			// given
			SignupRequestDto requestDto = new SignupRequestDto(USERNAME, PASSWORD, NICKNAME);
			userRepository.save(new User(requestDto));

			LoginRequestDto loginRequestDto = new LoginRequestDto(USERNAME, "BAD" + PASSWORD);

			// when & then
			mockMvc.perform(patch("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginRequestDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_CREDENTIALS.getCode()))
				.andExpect(jsonPath("$.error.message").value(ErrorCode.INVALID_CREDENTIALS.getMessage()));
		}
	}

}
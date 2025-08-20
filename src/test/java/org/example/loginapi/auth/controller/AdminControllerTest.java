package org.example.loginapi.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.loginapi.auth.entity.User;
import org.example.loginapi.auth.enums.UserRole;
import org.example.loginapi.auth.repository.UserRepository;
import org.example.loginapi.common.exception.ErrorCode;
import org.example.loginapi.common.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;

	private static final String USERNAME = "test1234";
	private static final String PASSWORD = "test1234";
	private static final String NICKNAME = "nickname";

	private static final String ADMIN_USERNAME = "admin1234";
	private static final String ADMIN_PASSWORD = "admin1234";
	private static final String ADMIN_NICKNAME = "adminname";

	private static User USER;
	private static String USER_TOKEN;
	private static User ADMIN_USER;
	private static String ADMIN_TOKEN;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
		USER = userRepository.save(
			User.builder()
				.username(USERNAME)
				.password(PASSWORD)
				.nickname(NICKNAME)
				.role(UserRole.USER)
				.build()
		);

		USER_TOKEN = "Bearer " + jwtUtil.createToken(USER.getId(), USER.getRole());

		ADMIN_USER = userRepository.save(
			User.builder()
				.username(ADMIN_USERNAME)
				.password(ADMIN_PASSWORD)
				.nickname(ADMIN_NICKNAME)
				.role(UserRole.ADMIN)
				.build()
		);

		ADMIN_TOKEN = "Bearer " + jwtUtil.createToken(ADMIN_USER.getId(), ADMIN_USER.getRole());
	}

	@Nested
	@DisplayName("ADMIN 권한 부여 API 테스트")
	class SignupTest {
		@Test
		@DisplayName("관리자 권한 부여 성공 - 관리자가 요청")
		void successUpdateUserRole() throws Exception {
			// given

			// when & then
			mockMvc.perform(patch("/admin/users/{userId}/roles", USER.getId())
					.header("Authorization", ADMIN_TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value(USERNAME))
				.andExpect(jsonPath("$.nickname").value(NICKNAME))
				.andExpect(jsonPath("$.roles[0]").value(UserRole.ADMIN.toString()));
		}

		@Test
		@DisplayName("관리자 권한 부여 실패 - USER가 요청")
		void requestUpdateUserRoleWithUSER() throws Exception {
			// given

			// when & then
			mockMvc.perform(patch("/admin/users/{userId}/roles", USER.getId())
					.header("Authorization", USER_TOKEN))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error.code").value(ErrorCode.ACCESS_DENIED.getCode()))
				.andExpect(jsonPath("$.error.message").value(ErrorCode.ACCESS_DENIED.getMessage()));
		}

		@Test
		@DisplayName("관리자 권한 부여 실패 - 존재하지 않는 유저에게 요청")
		void requestUpdateUserRoleToNotFoundUser() throws Exception {
			// given

			// when & then
			mockMvc.perform(patch("/admin/users/{userId}/roles", -1L)
					.header("Authorization", ADMIN_TOKEN))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
				.andExpect(jsonPath("$.error.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
		}
	}
}
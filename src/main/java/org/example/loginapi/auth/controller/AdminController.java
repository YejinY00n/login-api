package org.example.loginapi.auth.controller;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.service.AdminService;
import org.example.loginapi.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;

	@Operation(summary = "권한 부여 api", description = "Admin 권한을 부여하는 api")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Admin 권한 부여 성공"),
		@ApiResponse(responseCode = "403", description = "권한 부족 (User 권한으로 접근)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자(userId)를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PatchMapping("/admin/users/{userId}/roles")
	public ResponseEntity<?> updateUserRole(@PathVariable Long userId) {
		return ResponseEntity.ok(adminService.updateUserRole(userId));
	}
}

package org.example.loginapi.auth.controller;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;

	@PatchMapping("/admin/users/{userId}/roles")
	public ResponseEntity<?> updateUserRole(@PathVariable Long userId) {
		return ResponseEntity.ok(adminService.updateUserRole(userId));
	}
}

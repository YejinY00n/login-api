package org.example.loginapi.auth.service;

import static org.example.loginapi.common.exception.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;

import org.example.loginapi.auth.dto.UpdateUserRoleResponseDto;
import org.example.loginapi.auth.entity.User;
import org.example.loginapi.auth.enums.UserRole;
import org.example.loginapi.auth.repository.UserRepository;
import org.example.loginapi.common.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final UserRepository userRepository;

	@Transactional
	public UpdateUserRoleResponseDto updateUserRole(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

		user.updateRole(UserRole.ADMIN);
		return new UpdateUserRoleResponseDto(user);
	}
}

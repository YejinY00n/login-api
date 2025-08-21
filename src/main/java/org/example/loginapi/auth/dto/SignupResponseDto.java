package org.example.loginapi.auth.dto;

import java.util.List;

import lombok.Getter;

import org.example.loginapi.auth.entity.User;

@Getter
public class SignupResponseDto {
	private String username;
	private String nickname;
	private List<String> roles;

	public SignupResponseDto(User user) {
		this.username = user.getUsername();
		this.nickname = user.getNickname();
		this.roles = List.of(user.getRole().toString());
	}
}

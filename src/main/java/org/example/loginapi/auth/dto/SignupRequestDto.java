package org.example.loginapi.auth.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
	private String username;
	private String password;
	private String nickname;
}

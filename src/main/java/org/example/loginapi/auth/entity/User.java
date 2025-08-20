package org.example.loginapi.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.loginapi.auth.dto.SignupRequestDto;
import org.example.loginapi.auth.enums.UserRole;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	public User(SignupRequestDto dto) {
		this.username = dto.getUsername();
		this.password = dto.getPassword();
		this.nickname = dto.getNickname();
		this.role = UserRole.USER;
	}

	public void updateRole(UserRole role) {
		this.role = role;
	}
}

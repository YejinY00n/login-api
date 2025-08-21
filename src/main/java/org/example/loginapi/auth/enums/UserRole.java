package org.example.loginapi.auth.enums;

import lombok.Getter;

@Getter
public enum UserRole {
	USER("User"), ADMIN("Admin");

	private final String label;

	UserRole(String label) {
		this.label = label;
	}
}

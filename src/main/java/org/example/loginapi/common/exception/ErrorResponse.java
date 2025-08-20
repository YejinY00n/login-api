package org.example.loginapi.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final ErrorCode error;

	public ErrorResponse(ErrorCode errorCode) {
		this.error = errorCode;
	}
}

package org.example.loginapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private ErrorDetail error;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ErrorDetail {
		private String code;
		private String message;
	}

	public ErrorResponse(ErrorCode errorCode) {
		this.error = new ErrorDetail(errorCode.name(), errorCode.getMessage());
	}
}

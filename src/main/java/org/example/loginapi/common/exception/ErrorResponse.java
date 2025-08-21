package org.example.loginapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	@Schema(description = "에러 정보", example = """
		{
		  "error": {
		    "code": "ERROR_CODE",
		    "message": "에러 메세지입니다."
		  }
		}
		""")
	private ErrorDetail error;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ErrorDetail {
		@Schema(description = "에러 코드", example = "ERROR_CODE")
		private String code;

		@Schema(description = "에러 메시지", example = "에러 메세지입니다.")
		private String message;
	}

	public ErrorResponse(ErrorCode errorCode) {
		this.error = new ErrorDetail(errorCode.name(), errorCode.getMessage());
	}
}

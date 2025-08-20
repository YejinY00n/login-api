package org.example.loginapi.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// CustomException Response
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleException(CustomException e) {
		return ResponseEntity
			.status(e.getErrorCode().getStatus())
			.body(new ErrorResponse(e.getErrorCode()));
	}

}

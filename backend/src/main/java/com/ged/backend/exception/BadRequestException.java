package com.ged.backend.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}

	public BadRequestException(String message, String errorCode) {
		super(message, errorCode, HttpStatus.BAD_REQUEST);
	}
}
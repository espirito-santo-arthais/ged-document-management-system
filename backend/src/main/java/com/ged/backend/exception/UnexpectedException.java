package com.ged.backend.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedException extends BaseException {

	private static final long serialVersionUID = 1L;

	public UnexpectedException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public UnexpectedException(String message, Throwable cause) {
		super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
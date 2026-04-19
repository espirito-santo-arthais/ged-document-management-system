package com.ged.backend.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends BaseException {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message, Throwable cause) {
		super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
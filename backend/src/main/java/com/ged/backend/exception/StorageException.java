package com.ged.backend.exception;

import org.springframework.http.HttpStatus;

public class StorageException extends BaseException {

	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
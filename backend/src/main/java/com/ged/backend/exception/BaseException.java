package com.ged.backend.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String errorCode;
	private final HttpStatus status;

	public BaseException(String message, HttpStatus status) {
		super(message);
		this.status = Objects.requireNonNull(status, "HttpStatus não pode ser nulo");
		this.errorCode = null;
	}

	public BaseException(String message, String errorCode, HttpStatus status) {
		super(message);
		this.status = Objects.requireNonNull(status, "HttpStatus não pode ser nulo");
		this.errorCode = errorCode;
	}

	public BaseException(String message, Throwable cause, HttpStatus status) {
		super(message, cause);
		this.status = Objects.requireNonNull(status, "HttpStatus não pode ser nulo");
		this.errorCode = null;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
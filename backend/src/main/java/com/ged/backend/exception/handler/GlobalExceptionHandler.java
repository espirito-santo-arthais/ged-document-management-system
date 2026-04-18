package com.ged.backend.exception.handler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.ged.backend.exception.BaseException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// BASE (todas suas exceptions customizadas)
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(
			BaseException ex,
			HttpServletRequest request) {

		log.warn("Handled exception: {}", ex.getMessage());

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(ex.getStatus().value())
				.error(ex.getStatus().getReasonPhrase())
				.message(ex.getMessage())
				.errorCode(ex.getErrorCode())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.status(ex.getStatus()).body(response);
	}

	// VALIDAÇÃO (Bean Validation)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		String message = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.joining("; "));

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(400)
				.error("Validation Error")
				.message(message)
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	// QUALQUER ERRO NÃO TRATADO
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(
			Exception ex,
			HttpServletRequest request) {

		log.error("Unhandled exception", ex);

		ErrorResponse response = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(500)
				.error("Internal Server Error")
				.message("Erro inesperado. Contate o suporte.")
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.internalServerError().body(response);
	}
}
package com.ged.backend.exception.handler;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ErrorResponse", description = "Resposta padrão para erros da API")
public class ErrorResponse {

	@Schema(description = "Data/hora do erro", example = "2026-04-18T10:30:00")
	private LocalDateTime timestamp;

	@Schema(description = "Código HTTP", example = "400")
	private int status;

	@Schema(description = "Tipo do erro", example = "Bad Request")
	private String error;

	@Schema(description = "Mensagem do erro", example = "ID inválido")
	private String message;

	@Schema(description = "Código interno do erro", example = "DOC-001")
	private String errorCode;

	@Schema(description = "Endpoint chamado", example = "/documents/123")
	private String path;
}
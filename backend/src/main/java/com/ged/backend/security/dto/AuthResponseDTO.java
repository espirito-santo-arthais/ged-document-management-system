package com.ged.backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "AuthResponse", description = "DTO de resposta contendo o token JWT")
public class AuthResponseDTO {

	@Schema(
			description = "Token JWT para autenticação nas requisições",
			example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String token;

	@Schema(
			description = "Tipo do token",
			example = "Bearer")
	private String type;
}
package com.ged.backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(name = "AuthRequest", description = "DTO utilizado para autenticação do usuário")
public class AuthRequestDTO {

	@Schema(
			description = "Nome de usuário",
			example = "admin@ged.com.br",
			requiredMode = Schema.RequiredMode.REQUIRED,
			maxLength = 100)
	@NotBlank(message = "O usuário é obrigatório")
	@Size(max = 100, message = "O usuário deve ter no máximo 100 caracteres")
	private String username;

	@Schema(
			description = "Senha do usuário",
			example = "admin123",
			requiredMode = Schema.RequiredMode.REQUIRED,
			maxLength = 100)
	@NotBlank(message = "A senha é obrigatória")
	@Size(max = 100, message = "A senha deve ter no máximo 100 caracteres")
	private String password;
}
package com.ged.backend.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ged.backend.security.dto.AuthRequestDTO;
import com.ged.backend.security.dto.AuthResponseDTO;
import com.ged.backend.security.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService service;

	@Operation(
			summary = "Autenticar usuário",
			description = "Realiza autenticação e retorna um token JWT válido")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
					@ApiResponse(responseCode = "400", description = "Usuário ou senha inválidos")
			})
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(
			@Valid
			@RequestBody AuthRequestDTO request) {

		log.info("Controller: login request recebido");

		AuthResponseDTO response = service.login(request);

		return ResponseEntity.ok(response);
	}
}
package com.ged.backend.security.service;

import org.springframework.stereotype.Service;

import com.ged.backend.exception.UnauthorizedException;
import com.ged.backend.security.dto.AuthRequestDTO;
import com.ged.backend.security.dto.AuthResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final JwtService jwtService;

	public AuthResponseDTO login(AuthRequestDTO request) {

		log.info("Tentativa de autenticação para usuário: {}", request.getUsername());

		// 🔹 Usuário ADMIN
		if ("admin@ged.com.br".equals(request.getUsername()) &&
				"admin123".equals(request.getPassword())) {

			String token = jwtService.generateToken("admin", "ADMIN");

			log.info("Autenticação realizada com sucesso para ADMIN");

			return AuthResponseDTO.builder()
					.token(token)
					.type("Bearer")
					.build();
		}

		// 🔹 Usuário USER
		if ("user@ged.com.br".equals(request.getUsername()) &&
				"user123".equals(request.getPassword())) {

			String token = jwtService.generateToken("user", "USER");

			log.info("Autenticação realizada com sucesso para USER");

			return AuthResponseDTO.builder()
					.token(token)
					.type("Bearer")
					.build();
		}

		log.warn("Falha na autenticação para usuário: {}", request.getUsername());

		throw new UnauthorizedException("Usuário ou senha inválidos");
	}
}
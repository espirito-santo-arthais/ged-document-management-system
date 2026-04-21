package com.ged.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@DisplayName("AuthController - Testes de Integração")
class AuthControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String LOGIN_URL = "/auth/login";

	// =========================
	// SETUP (CLEAN DATABASE ONLY)
	// =========================
	@BeforeEach
	void setup() {
		cleanDatabase();
	}

	private void cleanDatabase() {
		jdbcTemplate.execute("DELETE FROM document_versions");
		jdbcTemplate.execute("DELETE FROM document_tags");
		jdbcTemplate.execute("DELETE FROM documents");
	}

	// =========================
	// SUCCESS SCENARIOS
	// =========================
	@Nested
	@DisplayName("Cenários de sucesso")
	class SuccessScenarios {

		@Test
		@DisplayName("Deve autenticar com credenciais válidas (ADMIN)")
		void shouldLoginSuccessfullyWithAdmin() throws Exception {

			String body = """
					{
					    "username": "admin",
					    "password": "admin123"
					}
					""";

			mockMvc.perform(post(LOGIN_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.token").exists())
					.andExpect(jsonPath("$.token").isNotEmpty());
		}

		@Test
		@DisplayName("Deve autenticar com credenciais válidas (USER)")
		void shouldLoginSuccessfullyWithUser() throws Exception {

			String body = """
					{
					    "username": "user",
					    "password": "user123"
					}
					""";

			mockMvc.perform(post(LOGIN_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.token").exists())
					.andExpect(jsonPath("$.token").isNotEmpty());
		}
	}

	// =========================
	// ERROR SCENARIOS
	// =========================
	@Nested
	@DisplayName("Cenários de erro")
	class ErrorScenarios {

		@Test
		@DisplayName("Deve retornar erro para credenciais inválidas")
		void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {

			String body = """
					{
					    "username": "admin",
					    "password": "senhaErrada"
					}
					""";

			mockMvc.perform(post(LOGIN_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body))
					.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("Deve retornar erro para usuário inexistente")
		void shouldReturnUnauthorizedForNonExistingUser() throws Exception {

			String body = """
					{
					    "username": "naoExiste",
					    "password": "123"
					}
					""";

			mockMvc.perform(post(LOGIN_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body))
					.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("Deve retornar erro quando body está vazio")
		void shouldReturnClientErrorWhenBodyIsEmpty() throws Exception {

			mockMvc.perform(post(LOGIN_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(""))
					.andExpect(status().is4xxClientError());
		}
	}
}
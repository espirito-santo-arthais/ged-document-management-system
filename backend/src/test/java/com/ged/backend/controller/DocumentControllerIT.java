package com.ged.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ged.backend.service.storage.StorageService;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("DocumentController - Testes de Integração")
class DocumentControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@MockBean
	private StorageService storageService;

	private static final String LOGIN_URL = "/auth/login";
	private static final String DOCUMENTS_URL = "/documents";

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
	// HELPERS
	// =========================
	private String getToken(String username, String password) throws Exception {

		String body = String.format("""
				{
				    "username": "%s",
				    "password": "%s"
				}
				""", username, password);

		String response = mockMvc.perform(post(LOGIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return JsonPath.read(response, "$.token");
	}

	// =========================
	// SECURITY TESTS
	// =========================
	@Nested
	@DisplayName("Segurança")
	class SecurityTests {

		@Test
		@DisplayName("Deve retornar 401 quando não enviar token")
		void shouldReturnUnauthorizedWithoutToken() throws Exception {

			mockMvc.perform(get(DOCUMENTS_URL))
					.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("Deve permitir acesso com token válido (ADMIN)")
		void shouldAllowAccessWithAdminToken() throws Exception {

			String token = getToken("admin", "admin123");

			mockMvc.perform(post("/documents/search")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}"))
					.andExpect(status().isOk());
		}

		@Test
		@DisplayName("Deve permitir acesso com token válido (USER)")
		void shouldAllowAccessWithUserToken() throws Exception {

			String token = getToken("user", "user123");

			mockMvc.perform(post("/documents/search")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}"))
					.andExpect(status().isOk());
		}
	}

	// =========================
	// AUTHORIZATION TESTS
	// =========================
	@Nested
	@DisplayName("Autorização por perfil")
	class AuthorizationTests {

		@Test
		@DisplayName("USER não deve conseguir criar documento (403)")
		void userShouldNotCreateDocument() throws Exception {

			String token = getToken("user", "user123");

			String body = """
					{
					    "title": "Documento Teste",
					    "description": "Descrição",
					    "owner": "user",
					    "tags": ["teste"]
					}
					""";

			mockMvc.perform(post(DOCUMENTS_URL)
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("ADMIN deve conseguir criar documento")
		void adminShouldCreateDocument() throws Exception {

			String token = getToken("admin", "admin123");

			String body = """
					{
					    "title": "Documento Admin",
					    "description": "Descrição",
					    "owner": "admin",
					    "tags": ["teste"]
					}
					""";

			mockMvc.perform(post(DOCUMENTS_URL)
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body))
					.andExpect(status().isCreated());
		}
	}
}
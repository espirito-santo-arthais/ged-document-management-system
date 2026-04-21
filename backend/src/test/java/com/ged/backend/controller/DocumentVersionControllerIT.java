package com.ged.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ged.backend.service.storage.StorageService;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@DisplayName("DocumentVersionController - Testes de Integração")
class DocumentVersionControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@MockBean
	private StorageService storageService;

	private static final String LOGIN_URL = "/auth/login";
	private static final String DOCUMENTS_URL = "/documents";

	@BeforeEach
	void setup() {
		when(storageService.upload(any()))
				.thenReturn("fake-storage-key");

		cleanDatabase();
	}

	private void cleanDatabase() {
		jdbcTemplate.execute("DELETE FROM document_versions");
		jdbcTemplate.execute("DELETE FROM document_tags");
		jdbcTemplate.execute("DELETE FROM documents");
	}

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
				.andReturn()
				.getResponse()
				.getContentAsString();

		return JsonPath.read(response, "$.token");
	}

	private UUID createDocument(String token) throws Exception {

		String body = """
				{
				    "title": "Doc Version",
				    "description": "Teste",
				    "owner": "admin",
				    "tags": ["v1"]
				}
				""";

		String response = mockMvc.perform(post(DOCUMENTS_URL)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andReturn()
				.getResponse()
				.getContentAsString();

		return UUID.fromString(JsonPath.read(response, "$.id"));
	}

	@Test
	@DisplayName("ADMIN deve conseguir fazer upload de versão")
	void shouldUploadVersion() throws Exception {

		String token = getToken("admin", "admin123");
		UUID documentId = createDocument(token);

		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.pdf",
				"application/pdf",
				"conteudo".getBytes(StandardCharsets.UTF_8));

		mockMvc.perform(multipart(DOCUMENTS_URL + "/{id}/versions", documentId)
				.file(file)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve listar versões")
	void shouldListVersions() throws Exception {

		String token = getToken("admin", "admin123");
		UUID documentId = createDocument(token);

		mockMvc.perform(get(DOCUMENTS_URL + "/{id}/versions", documentId)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve retornar 401 sem token")
	void shouldReturnUnauthorized() throws Exception {

		mockMvc.perform(get(DOCUMENTS_URL + "/{id}/versions", UUID.randomUUID()))
				.andExpect(status().isUnauthorized());
	}
}
package com.ged.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

	@Autowired
	Environment env;

	@Test
	void contextLoads() {
		// se subir sem erro, já é sucesso
	}

	@Test
	void shouldLoadDatasourceConfig() {
		String url = env.getProperty("spring.datasource.url");

		assertNotNull(url); // suficiente
	}
}
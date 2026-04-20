package com.ged.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("GED - Document Management System")
						.version("1.0.0")
						.description("Sistema de gerenciamento de documentos com versionamento e armazenamento em MinIO")
						.contact(new Contact()
								.name("Raimundo do Espírito Santo")
								.email("espirito.santo.arthais@outlook.com")
								.url("https://www.linkedin.com/in/raimundo-do-esp%C3%ADrito-santo-37306b282/")));
	}
}
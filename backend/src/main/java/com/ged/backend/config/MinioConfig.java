package com.ged.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {

	@Bean
	public MinioClient minioClient(
			@Value("${MINIO_ENDPOINT}") String endpoint,
			@Value("${MINIO_ROOT_USER}") String accessKey,
			@Value("${MINIO_ROOT_PASSWORD}") String secretKey) {

		return MinioClient.builder()
				.endpoint(endpoint)
				.credentials(accessKey, secretKey)
				.build();
	}
}
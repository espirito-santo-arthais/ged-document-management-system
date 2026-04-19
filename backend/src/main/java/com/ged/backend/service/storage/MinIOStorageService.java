package com.ged.backend.service.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinIOStorageService implements StorageService {

	@Override
	public String upload(MultipartFile file) {
		log.info("Iniciando upload de arquivo. Nome: {}", file.getOriginalFilename());

		try {
			// Simula geração de chave única
			String storageKey = "mock/" + UUID.randomUUID();

			log.info("Upload simulado com sucesso. Key: {}", storageKey);

			return storageKey;

		} catch (Exception ex) {
			String errorMessage = String.format(
					"Erro ao simular upload no storage. File: %s",
					file.getOriginalFilename());
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}

	@Override
	public InputStream download(String storageKey) {
		log.info("Iniciando download de arquivo. Key: {}", storageKey);

		try {
			// Simula conteúdo do arquivo
			String fakeContent = "Arquivo simulado para key: " + storageKey;

			log.info("Download simulado com sucesso. Key: {}", storageKey);

			return new ByteArrayInputStream(fakeContent.getBytes());

		} catch (Exception ex) {
			String errorMessage = String.format(
					"Erro ao simular download no storage. Key: %s",
					storageKey);
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}

	@Override
	public void delete(String storageKey) {
		log.info("Iniciando exclusão de arquivo. Key: {}", storageKey);

		try {
			// Simulação apenas log
			log.info("Exclusão simulada com sucesso. Key: {}", storageKey);

		} catch (Exception ex) {
			String errorMessage = String.format(
					"Erro ao simular exclusão no storage. Key: %s",
					storageKey);
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}
}
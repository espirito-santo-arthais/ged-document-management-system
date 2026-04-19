package com.ged.backend.service.storage;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	/**
	 * Realiza upload de um arquivo para o storage
	 * 
	 * @param file arquivo enviado
	 * @return chave única do arquivo no storage
	 */
	String upload(MultipartFile file);

	/**
	 * Realiza download de um arquivo do storage
	 * 
	 * @param storageKey identificador do arquivo
	 * @return stream do arquivo
	 */
	InputStream download(String storageKey);

	/**
	 * Remove um arquivo do storage
	 * 
	 * @param storageKey identificador do arquivo
	 */
	void delete(String storageKey);
}
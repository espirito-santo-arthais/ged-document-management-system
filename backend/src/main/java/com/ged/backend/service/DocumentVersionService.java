package com.ged.backend.service;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ged.backend.domain.dto.documentversion.DocumentVersionResponseDTO;
import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.entity.DocumentVersion;
import com.ged.backend.exception.BadRequestException;
import com.ged.backend.exception.DatabaseException;
import com.ged.backend.exception.ResourceNotFoundException;
import com.ged.backend.exception.StorageException;
import com.ged.backend.mapper.DocumentVersionMapper;
import com.ged.backend.repository.DocumentRepository;
import com.ged.backend.repository.DocumentVersionRepository;
import com.ged.backend.service.storage.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVersionService {

	private final DocumentVersionRepository documentVersionRepository;
	private final DocumentRepository documentRepository;
	private final StorageService storageService;

	public DocumentVersionResponseDTO upload(UUID documentId, MultipartFile file) {
		log.info("Iniciando upload de versão de documento. DocumentId: {}, File: {}",
				documentId, file != null ? file.getOriginalFilename() : null);

		if (documentId == null) {
			String msg = "ID do documento é obrigatório para upload";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		if (file == null || file.isEmpty()) {
			String msg = "Arquivo é obrigatório para upload";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		Set<String> allowedTypes = Set.of(
				"application/pdf",
				"image/png",
				"image/jpeg");

		String contentType = file.getContentType();

		if (contentType == null || !allowedTypes.contains(contentType)) {
			String msg = "Tipo de arquivo inválido. Permitidos: PDF, PNG, JPEG";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		String fileName = file.getOriginalFilename();

		if (fileName == null || fileName.isBlank()) {
			String msg = "Nome do arquivo é obrigatório";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		String lowerFileName = fileName.toLowerCase();

		if (!(lowerFileName.endsWith(".pdf")
				|| lowerFileName.endsWith(".png")
				|| lowerFileName.endsWith(".jpg")
				|| lowerFileName.endsWith(".jpeg"))) {
			String msg = "Extensão de arquivo inválida. Permitidas: PDF, PNG, JPG ou JPEG";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			// =========================
			// 1. BUSCA DOCUMENTO + VERSÃO
			// =========================
			Document document;
			Integer nextVersion;

			try {
				document = documentRepository.findById(documentId)
						.orElseThrow(() -> {
							String msg = String.format("Documento não encontrado. ID: %s", documentId);
							log.error(msg);
							return new ResourceNotFoundException(msg);
						});

				nextVersion = documentVersionRepository
						.findTopByDocumentIdOrderByVersionDesc(documentId)
						.map(v -> v.getVersion() + 1)
						.orElse(1);

			} catch (DataAccessException ex) {
				String errorMessage = String.format(
						"Erro de banco de dados ao preparar upload. DocumentId: %s",
						documentId);
				log.error(errorMessage, ex);
				throw new DatabaseException(errorMessage, ex);
			}

			// =========================
			// 2. UPLOAD STORAGE
			// =========================
			String storageKey;
			try {
				storageKey = storageService.upload(file);
			} catch (Exception ex) {
				String errorMessage = String.format(
						"Erro ao realizar upload no storage. DocumentId: %s, File: %s",
						documentId, file.getOriginalFilename());
				log.error(errorMessage, ex);
				throw new StorageException(errorMessage, ex);
			}

			// =========================
			// 3. SALVAR NO BANCO
			// =========================
			try {
				DocumentVersion entity = DocumentVersion.builder()
						.document(document)
						.version(nextVersion)
						.fileName(file.getOriginalFilename())
						.contentType(file.getContentType())
						.size(file.getSize())
						.storageKey(storageKey)
						.build();

				DocumentVersion saved = documentVersionRepository.save(entity);

				DocumentVersionResponseDTO response = DocumentVersionMapper.toResponse(saved);

				log.info("Upload realizado com sucesso. DocumentId: {}, Versão: {}, Key: {}",
						documentId, nextVersion, storageKey);

				return response;

			} catch (DataAccessException ex) {
				// rollback manual do storage (MUITO IMPORTANTE)
				try {
					storageService.delete(storageKey);
					log.warn("Rollback do storage realizado. Key: {}", storageKey);
				} catch (Exception rollbackEx) {
					log.error("Erro ao realizar rollback do storage. Key: {}", storageKey, rollbackEx);
					// Não conseguiu limpar o storage, o pessoal de HelpDesk vai ter que localizar
					// os logs,
					// obter o storageKey e excluir manualmente o arquivo do storage.
				}

				String errorMessage = String.format(
						"Erro de banco de dados ao salvar versão após upload. DocumentId: %s",
						documentId);
				log.error(errorMessage, ex);
				throw new DatabaseException(errorMessage, ex);
			}
		} catch (StorageException | DatabaseException ex) {
			throw ex; // já tratados
		} catch (Exception ex) {
			String errorMessage = String.format(
					"Erro inesperado ao realizar upload de versão. DocumentId: %s",
					documentId);
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}

	public InputStream download(UUID documentId, Integer version) {
		log.info("Iniciando download de versão. DocumentId: {}, Version: {}", documentId, version);

		if (documentId == null) {
			String msg = "ID do documento é obrigatório para download";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		if (version == null) {
			String msg = "Versão é obrigatória para download";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			// =========================
			// 1. BUSCA NO BANCO
			// =========================
			DocumentVersion entity;

			try {
				entity = documentVersionRepository
						.findByDocumentIdAndVersion(documentId, version)
						.orElseThrow(() -> {
							String msg = String.format(
									"Versão não encontrada. DocumentId: %s, Version: %s",
									documentId, version);
							log.error(msg);
							return new ResourceNotFoundException(msg);
						});
			} catch (DataAccessException ex) {
				String errorMessage = String.format(
						"Erro de banco de dados ao buscar versão para download. DocumentId: %s, Version: %s",
						documentId, version);
				log.error(errorMessage, ex);
				throw new DatabaseException(errorMessage, ex);
			}

			// =========================
			// 2. DOWNLOAD STORAGE
			// =========================
			try {
				InputStream stream = storageService.download(entity.getStorageKey());

				log.info("Download realizado com sucesso. DocumentId: {}, Version: {}",
						documentId, version);

				return stream;
			} catch (Exception ex) {
				String errorMessage = String.format(
						"Erro ao realizar download no storage. DocumentId: %s, Version: %s, Key: %s",
						documentId, version, entity.getStorageKey());
				log.error(errorMessage, ex);
				throw new StorageException(errorMessage, ex);
			}
		} catch (StorageException | DatabaseException ex) {
			throw ex;
		} catch (Exception ex) {
			String errorMessage = String.format(
					"Erro inesperado ao realizar download. DocumentId: %s, Version: %s",
					documentId, version);
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}

	public List<DocumentVersionResponseDTO> listVersions(UUID documentId) {
		log.info("Listando versões do documento. DocumentId: {}", documentId);

		if (documentId == null) {
			String msg = "ID do documento é obrigatório para listar versões";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			List<DocumentVersion> versions = documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId);

			List<DocumentVersionResponseDTO> response = versions.stream()
					.map(DocumentVersionMapper::toResponse)
					.toList();

			log.info("Versões listadas com sucesso. DocumentId: {}, Total: {}",
					documentId, response.size());

			return response;
		} catch (DataAccessException ex) {
			String errorMessage = String.format(
					"Erro de banco de dados ao listar versões. DocumentId: %s",
					documentId);
			log.error(errorMessage, ex);
			throw new DatabaseException(errorMessage, ex);
		}
	}

	public void deleteVersion(UUID documentId, Integer version) {
		log.info("Iniciando exclusão de versão. DocumentId: {}, Version: {}", documentId, version);

		if (documentId == null) {
			String msg = "ID do documento é obrigatório para exclusão";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		if (version == null) {
			String msg = "Versão é obrigatória para exclusão";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			// =========================
			// 1. BUSCA NO BANCO
			// =========================
			DocumentVersion entity;
			List<DocumentVersion> allVersions;

			try {
				entity = documentVersionRepository
						.findByDocumentIdAndVersion(documentId, version)
						.orElseThrow(() -> {
							String msg = String.format(
									"Versão não encontrada. DocumentId: %s, Version: %s",
									documentId, version);
							log.error(msg);
							return new ResourceNotFoundException(msg);
						});

				allVersions = documentVersionRepository
						.findByDocumentIdOrderByVersionDesc(documentId);

				if (allVersions.size() <= 1) {
					String msg = "Não é permitido excluir a única versão do documento";
					log.error(msg);
					throw new BadRequestException(msg);
				}

				Integer latestVersion = allVersions.get(0).getVersion();

				if (version.equals(latestVersion)) {
					String msg = "Não é permitido excluir a versão mais recente do documento";
					log.error(msg);
					throw new BadRequestException(msg);
				}
			} catch (DataAccessException ex) {
				String errorMessage = String.format(
						"Erro de banco de dados ao preparar exclusão de versão. DocumentId: %s, Version: %s",
						documentId, version);
				log.error(errorMessage, ex);
				throw new DatabaseException(errorMessage, ex);
			}

			// =========================
			// 2. DELETE STORAGE
			// =========================
			try {
				storageService.delete(entity.getStorageKey());
			} catch (Exception ex) {
				String errorMessage = String.format(
						"Erro ao excluir arquivo no storage. DocumentId: %s, Version: %s, Key: %s",
						documentId, version, entity.getStorageKey());
				log.error(errorMessage, ex);
				throw new StorageException(errorMessage, ex);
			}

			// =========================
			// 3. DELETE BANCO
			// =========================
			try {
				documentVersionRepository.delete(entity);

				log.info("Versão excluída com sucesso. DocumentId: {}, Version: {}",
						documentId, version);
			} catch (DataAccessException ex) {
				// aqui não tem rollback real possível
				log.error("Inconsistência detectada: arquivo removido, mas falha ao remover do banco. DocumentId: {}, Version: {}",
						documentId, version, ex);

				String errorMessage = String.format(
						"Erro de banco de dados ao excluir versão após remoção no storage. DocumentId: %s, Version: %s",
						documentId, version);

				throw new DatabaseException(errorMessage, ex);
			}
		} catch (StorageException | DatabaseException ex) {
			throw ex;
		} catch (Exception ex) {
			String errorMessage = String.format(
					"Erro inesperado ao excluir versão. DocumentId: %s, Version: %s",
					documentId, version);
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}
}
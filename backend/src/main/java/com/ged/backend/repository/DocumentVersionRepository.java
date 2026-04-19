package com.ged.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ged.backend.domain.entity.DocumentVersion;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, UUID> {

	/**
	 * Retorna a última versão (maior número de versão) de um documento
	 */
	Optional<DocumentVersion> findTopByDocumentIdOrderByVersionDesc(UUID documentId);

	/**
	 * Retorna todas as versões de um documento ordenadas por versão (desc)
	 */
	List<DocumentVersion> findByDocumentIdOrderByVersionDesc(UUID documentId);

	/**
	 * Busca uma versão específica de um documento
	 */
	Optional<DocumentVersion> findByDocumentIdAndVersion(UUID documentId, Integer version);
}
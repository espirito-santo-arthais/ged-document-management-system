package com.ged.backend.mapper;

import com.ged.backend.domain.dto.documentversion.DocumentVersionResponseDTO;
import com.ged.backend.domain.entity.DocumentVersion;

public class DocumentVersionMapper {

	private DocumentVersionMapper() {
		// Evita instanciação
	}

	public static DocumentVersionResponseDTO toResponse(DocumentVersion entity) {
		if (entity == null) {
			return null;
		}

		return DocumentVersionResponseDTO.builder()
				.id(entity.getId())
				.version(entity.getVersion())
				.fileName(entity.getFileName())
				.contentType(entity.getContentType())
				.size(entity.getSize())
				.createdAt(entity.getCreatedAt())
				.build();
	}
}

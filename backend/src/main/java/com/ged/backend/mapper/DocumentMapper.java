package com.ged.backend.mapper;

import com.ged.backend.domain.dto.document.DocumentCreateRequestDTO;
import com.ged.backend.domain.dto.document.DocumentResponseDTO;
import com.ged.backend.domain.dto.document.DocumentUpdateRequestDTO;
import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.enums.DocumentStatusEnum;

public class DocumentMapper {

	private DocumentMapper() {
		// utility class
	}

	// =========================
	// CREATE
	// =========================
	public static Document toEntity(DocumentCreateRequestDTO dto) {
		if (dto == null) {
			return null;
		}

		return Document.builder()
				.title(dto.getTitle())
				.description(dto.getDescription())
				.owner(dto.getOwner())
				.status(DocumentStatusEnum.DRAFT) // regra de negócio
				.tags(dto.getTags())
				.build();
	}

	// =========================
	// UPDATE (PATCH)
	// =========================
	public static void updateEntity(Document entity, DocumentUpdateRequestDTO dto) {
		if (dto.getTitle().isPresent()) {
			entity.setTitle(dto.getTitle().orElse(null));
		}
		if (dto.getDescription().isPresent()) {
			entity.setDescription(dto.getDescription().orElse(null));
		}
		if (dto.getOwner().isPresent()) {
			entity.setOwner(dto.getOwner().orElse(null));
		}
		if (dto.getStatus().isPresent()) {
			entity.setStatus(dto.getStatus().orElse(null));
		}
		if (dto.getTags().isPresent()) {
			entity.setTags(dto.getTags().orElse(null));
		}
	}

	// =========================
	// RESPONSE
	// =========================
	public static DocumentResponseDTO toResponse(Document entity) {
		if (entity == null) {
			return null;
		}

		return DocumentResponseDTO.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.owner(entity.getOwner())
				.status(entity.getStatus())
				.tags(entity.getTags())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}
package com.ged.backend.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ged.backend.domain.dto.document.*;
import com.ged.backend.domain.entity.Document;
import com.ged.backend.mapper.DocumentMapper;
import com.ged.backend.repository.DocumentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

	private final DocumentRepository repository;

	public DocumentResponseDTO create(DocumentCreateRequestDTO dto) {
		log.info("Creating document with title: {}", dto.getTitle());

		Document entity = DocumentMapper.toEntity(dto);

		Document saved = repository.save(entity);

		return DocumentMapper.toResponse(saved);
	}

	public DocumentResponseDTO update(DocumentUpdateRequestDTO dto) {
		log.info("Updating document id: {}", dto.getId());

		Document entity = repository.findById(dto.getId())
				.orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));

		DocumentMapper.updateEntity(entity, dto);

		Document updated = repository.save(entity);

		return DocumentMapper.toResponse(updated);
	}

	public void delete(UUID id) {
		log.info("Deleting document id: {}", id);

		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Documento não encontrado");
		}

		repository.deleteById(id);
	}

	public DocumentResponseDTO findById(UUID id) {
		log.info("Finding document id: {}", id);

		Document entity = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));

		return DocumentMapper.toResponse(entity);
	}

	public Page<DocumentResponseDTO> search(DocumentSearchDTO dto, Pageable pageable) {
		log.info("Searching documents with filters: {}", dto);

		Page<Document> page = dto.getStartsWith()
				? repository.searchStartingWith(
						dto.getTitle(),
						dto.getStatus(),
						dto.getOwner(),
						dto.getCreatedAfter(),
						dto.getCreatedBefore(),
						dto.getUpdatedAfter(),
						dto.getUpdatedBefore(),
						dto.getTags(),
						pageable)
				: repository.searchContaining(
						dto.getTitle(),
						dto.getStatus(),
						dto.getOwner(),
						dto.getCreatedAfter(),
						dto.getCreatedBefore(),
						dto.getUpdatedAfter(),
						dto.getUpdatedBefore(),
						dto.getTags(),
						pageable);

		return page.map(DocumentMapper::toResponse);
	}
}
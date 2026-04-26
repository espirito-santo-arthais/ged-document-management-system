package com.ged.backend.service;

import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ged.backend.domain.dto.document.DocumentCreateRequestDTO;
import com.ged.backend.domain.dto.document.DocumentResponseDTO;
import com.ged.backend.domain.dto.document.DocumentSearchDTO;
import com.ged.backend.domain.dto.document.DocumentUpdateRequestDTO;
import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.enums.SearchTypeEnum;
import com.ged.backend.exception.BadRequestException;
import com.ged.backend.exception.BaseException;
import com.ged.backend.exception.DatabaseException;
import com.ged.backend.exception.ResourceNotFoundException;
import com.ged.backend.exception.UnexpectedException;
import com.ged.backend.mapper.DocumentMapper;
import com.ged.backend.repository.DocumentRepository;
import com.ged.backend.specification.DocumentSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

	private final DocumentRepository repository;

	@Transactional
	public DocumentResponseDTO create(DocumentCreateRequestDTO dto) {
		log.info("Iniciando criação de documento. Título: {}", dto.getTitle());

		try {
			Document entity = DocumentMapper.toEntity(dto);

			Document saved = repository.save(entity);

			DocumentResponseDTO response = DocumentMapper.toResponse(saved);

			log.info("Documento criado com sucesso. ID, Título, Status: {}, {}, {}",
					response.getId(), response.getTitle(), response.getStatus());

			return response;
		} catch (BaseException ex) {
			throw ex; // mantém regra de negócio
		} catch (DataAccessException ex) {
			String errorMessage = String.format("Erro de banco de dados ao criar documento. Título: %s", dto.getTitle());
			log.error(errorMessage, ex);
			throw new DatabaseException(errorMessage, ex);
		} catch (Exception ex) {
			String errorMessage = String.format("Erro inesperado ao criar documento. Título: %s", dto.getTitle());
			log.error(errorMessage, ex);
			throw new UnexpectedException(errorMessage, ex);
		}
	}

	@Transactional
	public DocumentResponseDTO update(UUID id, DocumentUpdateRequestDTO dto) {
		log.info("Iniciando atualização de documento. ID (path): {}, ID (body): {}", id, dto.getId());

		if (id == null) {
			String msg = "ID do documento é obrigatório na URL";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		if (dto.getId() == null) {
			String msg = "ID do documento é obrigatório no corpo da requisição";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		if (!id.equals(dto.getId())) {
			String msg = String.format("ID da URL (%s) é diferente do ID do corpo (%s)", id, dto.getId());
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			Document entity = repository.findById(id)
					.orElseThrow(() -> {
						String msg = String.format("Documento não encontrado. ID: %s", id);
						log.error(msg);
						return new ResourceNotFoundException(msg);
					});

			DocumentMapper.updateEntity(entity, dto);

			Document updated = repository.save(entity);

			DocumentResponseDTO response = DocumentMapper.toResponse(updated);

			log.info("Documento atualizado com sucesso. ID, Título, Status: {}, {}, {}",
					response.getId(), response.getTitle(), response.getStatus());

			return response;
		} catch (BaseException ex) {
			throw ex; // mantém regra de negócio
		} catch (DataAccessException ex) {
			String errorMessage = String.format("Erro de banco de dados ao atualizar documento. Título: %s", dto.getTitle());
			log.error(errorMessage, ex);
			throw new DatabaseException(errorMessage, ex);
		} catch (Exception ex) {
			String errorMessage = String.format("Erro inesperado ao atualizar documento. Título: %s", dto.getTitle());
			log.error(errorMessage, ex);
			throw new UnexpectedException(errorMessage, ex);
		}
	}

	@Transactional
	public void delete(UUID id) {
		log.info("Iniciando exclusão de documento. ID: {}", id);

		if (id == null) {
			String msg = "ID do documento é obrigatório para exclusão";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			if (!repository.existsById(id)) {
				String msg = String.format("Documento não encontrado. ID: %s", id);
				log.error(msg);
				throw new ResourceNotFoundException(msg);
			}

			repository.deleteById(id);

			log.info("Documento excluído com sucesso. ID: {}", id);
		} catch (BaseException ex) {
			throw ex; // mantém regra de negócio
		} catch (DataAccessException ex) {
			String errorMessage = String.format("Erro de banco de dados ao excluir documento. ID: %s", id);
			log.error(errorMessage, ex);
			throw new DatabaseException(errorMessage, ex);
		} catch (Exception ex) {
			String errorMessage = String.format("Erro inesperado ao excluir documento. ID: %s", id);
			log.error(errorMessage, ex);
			throw new UnexpectedException(errorMessage, ex);
		}
	}

	public DocumentResponseDTO findById(UUID id) {
		log.info("Iniciando busca de documento. ID: {}", id);

		if (id == null) {
			String msg = "ID do documento é obrigatório para consulta";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			Document entity = repository.findById(id)
					.orElseThrow(() -> {
						String msg = String.format("Documento não encontrado. ID: %s", id);
						log.error(msg);
						return new ResourceNotFoundException(msg);
					});

			DocumentResponseDTO response = DocumentMapper.toResponse(entity);

			log.info("Documento encontrado com sucesso. ID, Título, Status: {}, {}, {}",
					response.getId(), response.getTitle(), response.getStatus());

			return response;
		} catch (BaseException ex) {
			throw ex; // mantém regra de negócio
		} catch (DataAccessException ex) {
			String errorMessage = String.format("Erro de banco de dados ao buscar documento. ID: %s", id);
			log.error(errorMessage, ex);
			throw new DatabaseException(errorMessage, ex);
		} catch (Exception ex) {
			String errorMessage = String.format("Erro inesperado ao buscar documento. ID: %s", id);
			log.error(errorMessage, ex);
			throw new UnexpectedException(errorMessage, ex);
		}
	}

	public Page<DocumentResponseDTO> search(DocumentSearchDTO dto, Pageable pageable) {
		log.info("Iniciando busca de documentos. Filtros: {}", dto);

		if (dto == null) {
			String msg = "Parâmetros de busca não podem ser nulos";
			log.error(msg);
			throw new BadRequestException(msg);
		}

		try {
			Specification<Document> spec = Specification.where(null);

			// TITLE / DESCRIPTION (com SearchTypeEnum)
			if (dto.getSearchType() == SearchTypeEnum.STARTS_WITH) {
				spec = spec.and(DocumentSpecification.titleOrDescriptionStartsWith(dto.getTitle()));
			} else {
				spec = spec.and(DocumentSpecification.titleOrDescriptionContains(dto.getTitle()));
			}

			// STATUS
			spec = spec.and(DocumentSpecification.statusEquals(dto.getStatus()));

			// OWNER
			spec = spec.and(DocumentSpecification.ownerEquals(dto.getOwner()));

			// CREATED
			spec = spec.and(DocumentSpecification.createdAfter(dto.getCreatedAfter()));
			spec = spec.and(DocumentSpecification.createdBefore(dto.getCreatedBefore()));

			// UPDATED
			spec = spec.and(DocumentSpecification.updatedAfter(dto.getUpdatedAfter()));
			spec = spec.and(DocumentSpecification.updatedBefore(dto.getUpdatedBefore()));

			// TAGS
			spec = spec.and(DocumentSpecification.hasTags(dto.getTags()));

			Page<Document> page = repository.findAll(spec, pageable);

			Page<DocumentResponseDTO> response = page.map(DocumentMapper::toResponse);

			log.info("Busca realizada com sucesso. Total encontrados: {}", response.getTotalElements());

			return response;
		} catch (BaseException ex) {
			throw ex; // mantém regra de negócio
		} catch (DataAccessException ex) {
			String errorMessage = String.format("Erro de banco de dados ao buscar documentos. Filtros: %s", dto);
			log.error(errorMessage, ex);
			throw new DatabaseException(errorMessage, ex);
		} catch (Exception ex) {
			String errorMessage = String.format("Erro inesperado ao buscar documentos. Filtros: %s", dto);
			log.error(errorMessage, ex);
			throw new UnexpectedException(errorMessage, ex);
		}
	}
}
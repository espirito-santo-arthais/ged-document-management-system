package com.ged.backend.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ged.backend.domain.dto.document.DocumentCreateRequestDTO;
import com.ged.backend.domain.dto.document.DocumentResponseDTO;
import com.ged.backend.domain.dto.document.DocumentSearchDTO;
import com.ged.backend.domain.dto.document.DocumentUpdateRequestDTO;
import com.ged.backend.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/documents")
@Tag(name = "Documentos", description = "API para gerenciamento de documentos e seus metadados")
public class DocumentController {

	private final DocumentService service;

	public DocumentController(DocumentService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar novo documento", description = "Cria um novo documento com metadados.")
	@ApiResponse(responseCode = "201", description = "Documento criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
	public ResponseEntity<DocumentResponseDTO> create(
			@Valid
			@RequestBody DocumentCreateRequestDTO dto) {

		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Atualizar documento", description = "Atualiza os metadados de um documento existente.")
	@ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso")
	@ApiResponse(responseCode = "404", description = "Documento não encontrado", content = @Content)
	public ResponseEntity<DocumentResponseDTO> update(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID id,
			@Valid
			@RequestBody DocumentUpdateRequestDTO dto) {

		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir documento", description = "Remove um documento pelo ID. Esta operação é irreversível.")
	@ApiResponse(responseCode = "204", description = "Documento excluído com sucesso")
	@ApiResponse(responseCode = "404", description = "Documento não encontrado", content = @Content)
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID id) {

		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar documento por ID", description = "Retorna os dados de um documento específico pelo seu ID.")
	@ApiResponse(responseCode = "200", description = "Documento encontrado")
	@ApiResponse(responseCode = "404", description = "Documento não encontrado", content = @Content)
	public ResponseEntity<DocumentResponseDTO> findById(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID id) {

		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping("/search")
	@Operation(summary = "Buscar documentos", description = """
			Realiza busca de documentos com filtros opcionais.

			Filtros disponíveis:
			- title: busca por título ou descrição
			- startsWith: define se a busca é por início ou contém
			- status: status do documento (DRAFT, PUBLISHED, ARCHIVED)
			- owner: proprietário do documento
			- createdAfter / createdBefore: intervalo de criação
			- updatedAfter / updatedBefore: intervalo de atualização
			- tags: lista de tags

			Paginação:
			- page: número da página (começa em 0)
			- size: quantidade de registros por página
			- sort: campo de ordenação (ex: title,asc)
			      """)
	@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	public ResponseEntity<Page<DocumentResponseDTO>> search(
			@Valid
			@RequestBody DocumentSearchDTO dto,
			@ParameterObject Pageable pageable) {

		return ResponseEntity.ok(service.search(dto, pageable));
	}
}
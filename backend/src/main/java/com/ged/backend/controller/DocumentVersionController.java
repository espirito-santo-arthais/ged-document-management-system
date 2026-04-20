package com.ged.backend.controller;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ged.backend.domain.dto.documentversion.DocumentVersionResponseDTO;
import com.ged.backend.exception.BaseException;
import com.ged.backend.exception.UnexpectedException;
import com.ged.backend.service.DocumentVersionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/documents/{documentId}/versions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Document Versions", description = "Gerenciamento de versões de documentos (upload, download e consulta)")
public class DocumentVersionController {

	private final DocumentVersionService service;

	// =========================
	// UPLOAD
	// =========================
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
			summary = "Upload de nova versão",
			description = "Realiza o upload de um arquivo (PDF, PNG ou JPG) criando uma nova versão para o documento.")
	@ApiResponses(
			value = {
					@ApiResponse(
							responseCode = "200",
							description = "Upload realizado com sucesso",
							content = @Content(schema = @Schema(implementation = DocumentVersionResponseDTO.class))),
					@ApiResponse(responseCode = "400", description = "Arquivo inválido"),
					@ApiResponse(responseCode = "404", description = "Documento não encontrado")
			})
	public ResponseEntity<DocumentVersionResponseDTO> upload(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID documentId,
			@Parameter(description = "Arquivo a ser enviado (PDF, PNG, JPG)", required = true)
			@RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok(service.upload(documentId, file));
	}

	// =========================
	// DELETE VERSION
	// =========================
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{version}")
	@Operation(
			summary = "Excluir versão",
			description = "Remove uma versão específica do documento. Não permite excluir a versão mais recente.")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "204", description = "Versão removida com sucesso"),
					@ApiResponse(responseCode = "400", description = "Regra de negócio violada"),
					@ApiResponse(responseCode = "404", description = "Documento ou versão não encontrada")
			})
	public ResponseEntity<Void> deleteVersion(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID documentId,
			@Parameter(description = "Número da versão", example = "1")
			@PathVariable Integer version) {
		service.deleteVersion(documentId, version);

		return ResponseEntity.noContent().build();
	}

	// =========================
	// GET VERSION METADATA
	// =========================
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/{version}/metadata")
	@Operation(
			summary = "Consultar metadados da versão",
			description = "Retorna os metadados de uma versão específica do documento.")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
					@ApiResponse(responseCode = "404", description = "Versão não encontrada")
			})
	public ResponseEntity<DocumentVersionResponseDTO> getVersion(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID documentId,
			@Parameter(description = "Número da versão", example = "1")
			@PathVariable Integer version) {
		return ResponseEntity.ok(service.findVersion(documentId, version));
	}

	// =========================
	// DOWNLOAD
	// =========================
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/{version}")
	@Operation(
			summary = "Download da versão",
			description = "Realiza o download do arquivo de uma versão específica do documento.")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Download realizado com sucesso"),
					@ApiResponse(responseCode = "404", description = "Arquivo não encontrado")
			})
	public ResponseEntity<byte[]> download(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID documentId,
			@Parameter(description = "Número da versão", example = "1")
			@PathVariable Integer version) {

		log.info("Download versão. DocumentId: {}, Version: {}", documentId, version);

		try (InputStream inputStream = service.download(documentId, version)) {

			// BUSCA METADATA (sem quebrar padrão)
			var metadata = service.findVersion(documentId, version);

			byte[] fileBytes = inputStream.readAllBytes();

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + metadata.getFileName() + "\"")
					.contentType(MediaType.parseMediaType(metadata.getContentType()))
					.contentLength(fileBytes.length)
					.body(fileBytes);

		} catch (BaseException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("Erro ao processar download", ex);
			throw new UnexpectedException("Erro ao processar download", ex);
		}
	}

	// =========================
	// LIST VERSIONS
	// =========================
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping
	@Operation(
			summary = "Listar versões",
			description = "Lista todas as versões de um documento.")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso"),
					@ApiResponse(responseCode = "404", description = "Documento não encontrado")
			})
	public ResponseEntity<List<DocumentVersionResponseDTO>> listVersions(
			@Parameter(description = "ID do documento", required = true)
			@PathVariable UUID documentId) {
		return ResponseEntity.ok(service.listVersions(documentId));
	}
}
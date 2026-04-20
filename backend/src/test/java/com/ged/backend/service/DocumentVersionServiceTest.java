package com.ged.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.entity.DocumentVersion;
import com.ged.backend.exception.BadRequestException;
import com.ged.backend.exception.DatabaseException;
import com.ged.backend.exception.ResourceNotFoundException;
import com.ged.backend.exception.StorageException;
import com.ged.backend.repository.DocumentRepository;
import com.ged.backend.repository.DocumentVersionRepository;
import com.ged.backend.service.storage.StorageService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do DocumentVersionService")
class DocumentVersionServiceTest {

	@Mock
	private DocumentVersionRepository documentVersionRepository;

	@Mock
	private DocumentRepository documentRepository;

	@Mock
	private StorageService storageService;

	@InjectMocks
	private DocumentVersionService service;

	private UUID documentId;

	@BeforeEach
	void setUp() {
		documentId = UUID.randomUUID();
	}

	@Nested
	@DisplayName("UPLOAD - Upload de versões de documento")
	class UploadTests {

		@Test
		@DisplayName("Deve realizar upload com sucesso quando dados são válidos")
		void shouldUploadSuccessfully() {
			// GIVEN
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("application/pdf");
			when(file.getOriginalFilename()).thenReturn("arquivo.pdf");
			when(file.getSize()).thenReturn(100L);

			Document document = Document.builder().id(documentId).build();

			when(documentRepository.findById(documentId))
					.thenReturn(Optional.of(document));

			when(documentVersionRepository.findTopByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(Optional.empty());

			when(storageService.upload(file)).thenReturn("storage-key");

			DocumentVersion saved = DocumentVersion.builder()
					.document(document)
					.version(1)
					.fileName("arquivo.pdf")
					.storageKey("storage-key")
					.build();

			when(documentVersionRepository.save(any(DocumentVersion.class)))
					.thenReturn(saved);

			// WHEN
			var response = service.upload(documentId, file);

			// THEN
			assertNotNull(response);
			verify(storageService, times(1)).upload(file);
			verify(documentVersionRepository, times(1)).save(any());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando documentId for nulo")
		void shouldThrowBadRequestWhenDocumentIdIsNull() {
			MultipartFile file = mock(MultipartFile.class);

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.upload(null, file));

			assertEquals("ID do documento é obrigatório para upload", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando arquivo for nulo")
		void shouldThrowBadRequestWhenFileIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.upload(documentId, null));

			assertEquals("Arquivo é obrigatório para upload", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando arquivo estiver vazio")
		void shouldThrowBadRequestWhenFileIsEmpty() {
			MultipartFile file = mock(MultipartFile.class);
			when(file.isEmpty()).thenReturn(true);

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.upload(documentId, file));

			assertEquals("Arquivo é obrigatório para upload", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando contentType for inválido")
		void shouldThrowBadRequestWhenContentTypeIsInvalid() {
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("text/plain");

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.upload(documentId, file));

			assertTrue(ex.getMessage().contains("Tipo de arquivo inválido"));
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando nome do arquivo for inválido")
		void shouldThrowBadRequestWhenFileNameIsInvalid() {
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("application/pdf");
			when(file.getOriginalFilename()).thenReturn("");

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.upload(documentId, file));

			assertEquals("Nome do arquivo é obrigatório", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando extensão for inválida")
		void shouldThrowBadRequestWhenExtensionIsInvalid() {
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("application/pdf");
			when(file.getOriginalFilename()).thenReturn("arquivo.txt");

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.upload(documentId, file));

			assertTrue(ex.getMessage().contains("Extensão de arquivo inválida"));
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando documento não existir")
		void shouldThrowResourceNotFoundWhenDocumentNotFound() {
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("application/pdf");
			when(file.getOriginalFilename()).thenReturn("arquivo.pdf");

			when(documentRepository.findById(documentId))
					.thenReturn(Optional.empty());

			assertThrows(
					ResourceNotFoundException.class,
					() -> service.upload(documentId, file));

			verify(documentRepository, times(1)).findById(documentId);
		}

		@Test
		@DisplayName("Deve lançar StorageException quando falhar upload no storage")
		void shouldThrowStorageExceptionWhenStorageFails() {
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("application/pdf");
			when(file.getOriginalFilename()).thenReturn("arquivo.pdf");

			Document document = Document.builder().id(documentId).build();

			when(documentRepository.findById(documentId))
					.thenReturn(Optional.of(document));

			when(documentVersionRepository.findTopByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(Optional.empty());

			when(storageService.upload(file))
					.thenThrow(new RuntimeException("Erro storage"));

			assertThrows(
					StorageException.class,
					() -> service.upload(documentId, file));
		}

		@Test
		@DisplayName("Deve realizar rollback no storage quando falhar persistência")
		void shouldRollbackStorageWhenDatabaseFails() {
			MultipartFile file = mock(MultipartFile.class);

			when(file.isEmpty()).thenReturn(false);
			when(file.getContentType()).thenReturn("application/pdf");
			when(file.getOriginalFilename()).thenReturn("arquivo.pdf");

			Document document = Document.builder().id(documentId).build();

			when(documentRepository.findById(documentId))
					.thenReturn(Optional.of(document));

			when(documentVersionRepository.findTopByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(Optional.empty());

			when(storageService.upload(file)).thenReturn("storage-key");

			when(documentVersionRepository.save(any()))
					.thenThrow(new DataIntegrityViolationException("Erro DB"));

			assertThrows(
					DatabaseException.class,
					() -> service.upload(documentId, file));

			verify(storageService, times(1)).delete("storage-key");
		}
	}

	@Nested
	@DisplayName("DELETE VERSION - Exclusão de versões de documento")
	class DeleteVersionTests {

		@Test
		@DisplayName("Deve excluir versão com sucesso quando não for a mais recente")
		void shouldDeleteVersionSuccessfully() {
			// GIVEN
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-1")
					.build();

			List<DocumentVersion> versions = List.of(
					DocumentVersion.builder().version(2).build(), // mais recente
					entity);

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(versions);

			doNothing().when(storageService).delete("key-1");
			doNothing().when(documentVersionRepository).delete(entity);

			// WHEN
			service.deleteVersion(documentId, version);

			// THEN
			verify(storageService, times(1)).delete("key-1");
			verify(documentVersionRepository, times(1)).delete(entity);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando documentId for nulo")
		void shouldThrowBadRequestWhenDocumentIdIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.deleteVersion(null, 1));

			assertEquals("ID do documento é obrigatório para exclusão", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando version for nula")
		void shouldThrowBadRequestWhenVersionIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.deleteVersion(documentId, null));

			assertEquals("Versão é obrigatória para exclusão", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando versão não existir")
		void shouldThrowResourceNotFoundWhenVersionNotExists() {
			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, 1))
					.thenReturn(Optional.empty());

			assertThrows(
					ResourceNotFoundException.class,
					() -> service.deleteVersion(documentId, 1));

			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, 1);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando for a única versão")
		void shouldThrowBadRequestWhenOnlyOneVersion() {
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-1")
					.build();

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(List.of(entity)); // só 1 versão

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.deleteVersion(documentId, version));

			assertEquals("Não é permitido excluir a única versão do documento", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando for a versão mais recente")
		void shouldThrowBadRequestWhenLatestVersion() {
			Integer version = 2;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-2")
					.build();

			List<DocumentVersion> versions = List.of(
					entity, // mais recente
					DocumentVersion.builder().version(1).build());

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(versions);

			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.deleteVersion(documentId, version));

			assertEquals("Não é permitido excluir a versão mais recente do documento", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar StorageException quando falhar exclusão no storage")
		void shouldThrowStorageExceptionWhenStorageFails() {
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-1")
					.build();

			List<DocumentVersion> versions = List.of(
					DocumentVersion.builder().version(2).build(),
					entity);

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(versions);

			doThrow(new RuntimeException("Erro storage"))
					.when(storageService).delete("key-1");

			assertThrows(
					StorageException.class,
					() -> service.deleteVersion(documentId, version));
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando falhar exclusão no banco após remover do storage")
		void shouldThrowDatabaseExceptionWhenDatabaseFailsAfterStorageDelete() {
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-1")
					.build();

			List<DocumentVersion> versions = List.of(
					DocumentVersion.builder().version(2).build(),
					entity);

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(versions);

			doNothing().when(storageService).delete("key-1");

			doThrow(new DataIntegrityViolationException("Erro DB"))
					.when(documentVersionRepository).delete(entity);

			assertThrows(
					DatabaseException.class,
					() -> service.deleteVersion(documentId, version));

			verify(storageService, times(1)).delete("key-1");
			verify(documentVersionRepository, times(1)).delete(entity);
		}
	}

	@Nested
	@DisplayName("FIND VERSION - Consulta de versão específica")
	class FindVersionTests {

		@Test
		@DisplayName("Deve retornar versão com sucesso quando existir")
		void shouldReturnVersionSuccessfully() {
			// GIVEN
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.fileName("arquivo.pdf")
					.build();

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			// WHEN
			var response = service.findVersion(documentId, version);

			// THEN
			assertNotNull(response);
			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, version);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando documentId for nulo")
		void shouldThrowBadRequestWhenDocumentIdIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.findVersion(null, 1));

			assertEquals("ID do documento é obrigatório para consulta", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando version for nula")
		void shouldThrowBadRequestWhenVersionIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.findVersion(documentId, null));

			assertEquals("Versão é obrigatória para consulta", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando versão não existir")
		void shouldThrowResourceNotFoundWhenVersionNotExists() {
			Integer version = 1;

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.empty());

			ResourceNotFoundException ex = assertThrows(
					ResourceNotFoundException.class,
					() -> service.findVersion(documentId, version));

			assertTrue(ex.getMessage().contains("Versão não encontrada"));

			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, version);
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro de banco")
		void shouldThrowDatabaseExceptionWhenDatabaseFails() {
			Integer version = 1;

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenThrow(new DataAccessException("Erro DB") {
					});

			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.findVersion(documentId, version));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao buscar de versão"));

			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, version);
		}
	}

	@Nested
	@DisplayName("DOWNLOAD - Download de versão de documento")
	class DownloadTests {

		@Test
		@DisplayName("Deve realizar download com sucesso quando versão existir")
		void shouldDownloadSuccessfully() {
			// GIVEN
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-1")
					.build();

			InputStream mockStream = new ByteArrayInputStream("conteudo".getBytes());

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(storageService.download("key-1"))
					.thenReturn(mockStream);

			// WHEN
			InputStream result = service.download(documentId, version);

			// THEN
			assertNotNull(result);
			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, version);
			verify(storageService, times(1))
					.download("key-1");
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando documentId for nulo")
		void shouldThrowBadRequestWhenDocumentIdIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.download(null, 1));

			assertEquals("ID do documento é obrigatório para download", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando version for nula")
		void shouldThrowBadRequestWhenVersionIsNull() {
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.download(documentId, null));

			assertEquals("Versão é obrigatória para download", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando versão não existir")
		void shouldThrowResourceNotFoundWhenVersionNotExists() {
			Integer version = 1;

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.empty());

			ResourceNotFoundException ex = assertThrows(
					ResourceNotFoundException.class,
					() -> service.download(documentId, version));

			assertTrue(ex.getMessage().contains("Versão não encontrada"));

			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, version);
		}

		@Test
		@DisplayName("Deve lançar StorageException quando falhar download no storage")
		void shouldThrowStorageExceptionWhenStorageFails() {
			Integer version = 1;

			DocumentVersion entity = DocumentVersion.builder()
					.version(version)
					.storageKey("key-1")
					.build();

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenReturn(Optional.of(entity));

			when(storageService.download("key-1"))
					.thenThrow(new RuntimeException("Erro storage"));

			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.download(documentId, version));

			assertTrue(ex.getMessage().contains("Erro ao realizar download no storage"));

			verify(storageService, times(1))
					.download("key-1");
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro ao buscar no banco")
		void shouldThrowDatabaseExceptionWhenDatabaseFails() {
			Integer version = 1;

			when(documentVersionRepository.findByDocumentIdAndVersion(documentId, version))
					.thenThrow(new DataAccessException("Erro DB") {
					});

			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.download(documentId, version));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao buscar versão para download"));

			verify(documentVersionRepository, times(1))
					.findByDocumentIdAndVersion(documentId, version);
		}
	}

	@Nested
	@DisplayName("LIST VERSIONS - Listagem de versões de documento")
	class ListVersionsTests {

		@Test
		@DisplayName("Deve listar versões com sucesso quando documentId for válido")
		void shouldListVersionsSuccessfully() {
			// GIVEN
			List<DocumentVersion> entities = List.of(
					DocumentVersion.builder().version(2).build(),
					DocumentVersion.builder().version(1).build());

			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(entities);

			// WHEN
			var response = service.listVersions(documentId);

			// THEN
			assertNotNull(response);
			assertEquals(2, response.size());

			verify(documentVersionRepository, times(1))
					.findByDocumentIdOrderByVersionDesc(documentId);
		}

		@Test
		@DisplayName("Deve retornar lista vazia quando não houver versões")
		void shouldReturnEmptyListWhenNoVersions() {
			// GIVEN
			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenReturn(List.of());

			// WHEN
			var response = service.listVersions(documentId);

			// THEN
			assertNotNull(response);
			assertTrue(response.isEmpty());

			verify(documentVersionRepository, times(1))
					.findByDocumentIdOrderByVersionDesc(documentId);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando documentId for nulo")
		void shouldThrowBadRequestWhenDocumentIdIsNull() {
			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.listVersions(null));

			assertEquals("ID do documento é obrigatório para listar versões", ex.getMessage());

			verify(documentVersionRepository, never())
					.findByDocumentIdOrderByVersionDesc(any());
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro no banco")
		void shouldThrowDatabaseExceptionWhenDatabaseFails() {
			// GIVEN
			when(documentVersionRepository.findByDocumentIdOrderByVersionDesc(documentId))
					.thenThrow(new DataAccessException("Erro DB") {
					});

			// WHEN & THEN
			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.listVersions(documentId));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao listar versões"));

			verify(documentVersionRepository, times(1))
					.findByDocumentIdOrderByVersionDesc(documentId);
		}
	}
}
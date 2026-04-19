package com.ged.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ged.backend.domain.dto.document.DocumentCreateRequestDTO;
import com.ged.backend.domain.dto.document.DocumentSearchDTO;
import com.ged.backend.domain.dto.document.DocumentUpdateRequestDTO;
import com.ged.backend.domain.entity.Document;
import com.ged.backend.domain.enums.DocumentStatusEnum;
import com.ged.backend.domain.enums.SearchTypeEnum;
import com.ged.backend.exception.BadRequestException;
import com.ged.backend.exception.DatabaseException;
import com.ged.backend.exception.ResourceNotFoundException;
import com.ged.backend.repository.DocumentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do DocumentService")
class DocumentServiceTest {

	@Mock
	private DocumentRepository repository;

	@InjectMocks
	private DocumentService service;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Nested
	@DisplayName("CREATE - Criação de documentos")
	class CreateTests {

		@Test
		@DisplayName("Deve criar um documento com sucesso quando os dados são válidos")
		void shouldCreateDocumentSuccessfully() {
			// GIVEN
			DocumentCreateRequestDTO dto = DocumentCreateRequestDTO.builder()
					.title("Contrato Teste")
					.build();

			Document entity = Document.builder()
					.id(UUID.randomUUID())
					.title(dto.getTitle())
					.build();

			when(repository.save(any(Document.class))).thenReturn(entity);

			// WHEN
			var response = service.create(dto);

			// THEN
			assertNotNull(response);
			assertEquals(dto.getTitle(), response.getTitle());

			verify(repository, times(1)).save(any(Document.class));
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro ao salvar no banco")
		void shouldThrowDatabaseExceptionWhenCreateFails() {
			// GIVEN
			DocumentCreateRequestDTO dto = DocumentCreateRequestDTO.builder()
					.title("Contrato Teste")
					.build();

			when(repository.save(any(Document.class)))
					.thenThrow(new DataIntegrityViolationException("Erro"));

			// WHEN & THEN
			assertThrows(
					DatabaseException.class,
					() -> service.create(dto));

			verify(repository, times(1)).save(any(Document.class));
		}
	}

	@Nested
	@DisplayName("UPDATE - Atualização de documentos")
	class UpdateTests {

		@Test
		@DisplayName("Deve atualizar um documento com sucesso quando o ID da URL e do corpo são iguais")
		void shouldUpdateDocumentSuccessfully() {
			// GIVEN
			UUID id = UUID.randomUUID();

			DocumentUpdateRequestDTO dto = DocumentUpdateRequestDTO.builder()
					.id(id)
					.title(JsonNullable.of("Contrato Atualizado"))
					.description(JsonNullable.of("Descrição atualizada"))
					.status(JsonNullable.of(DocumentStatusEnum.PUBLISHED))
					.owner(JsonNullable.of("user1"))
					.build();

			Document existing = Document.builder()
					.id(id)
					.title("Contrato Antigo")
					.description("Descrição antiga")
					.status(DocumentStatusEnum.DRAFT)
					.owner("user1")
					.build();

			Document saved = Document.builder()
					.id(id)
					.title("Contrato Atualizado")
					.description("Descrição atualizada")
					.status(DocumentStatusEnum.PUBLISHED)
					.owner("user1")
					.build();

			when(repository.findById(id)).thenReturn(Optional.of(existing));
			when(repository.save(any(Document.class))).thenReturn(saved);

			// WHEN
			var response = service.update(id, dto);

			// THEN
			assertNotNull(response);
			assertEquals(id, response.getId());
			assertEquals("Contrato Atualizado", response.getTitle());
			assertEquals("Descrição atualizada", response.getDescription());
			assertEquals(DocumentStatusEnum.PUBLISHED, response.getStatus());

			verify(repository, times(1)).findById(id);
			verify(repository, times(1)).save(existing);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando o ID da URL for nulo")
		void shouldThrowBadRequestExceptionWhenPathIdIsNull() {
			// GIVEN
			DocumentUpdateRequestDTO dto = DocumentUpdateRequestDTO.builder()
					.id(UUID.randomUUID())
					.title(JsonNullable.of("Contrato Atualizado"))
					.build();

			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.update(null, dto));

			assertEquals("ID do documento é obrigatório na URL", ex.getMessage());
			verify(repository, never()).findById(any());
			verify(repository, never()).save(any());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando o ID do corpo da requisição for nulo")
		void shouldThrowBadRequestExceptionWhenBodyIdIsNull() {
			// GIVEN
			UUID id = UUID.randomUUID();

			DocumentUpdateRequestDTO dto = DocumentUpdateRequestDTO.builder()
					.id(null)
					.title(JsonNullable.of("Contrato Atualizado"))
					.build();

			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.update(id, dto));

			assertEquals("ID do documento é obrigatório no corpo da requisição", ex.getMessage());
			verify(repository, never()).findById(any());
			verify(repository, never()).save(any());
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando o ID da URL for diferente do ID do corpo")
		void shouldThrowBadRequestExceptionWhenIdsAreDifferent() {
			// GIVEN
			UUID pathId = UUID.randomUUID();
			UUID bodyId = UUID.randomUUID();

			DocumentUpdateRequestDTO dto = DocumentUpdateRequestDTO.builder()
					.id(bodyId)
					.title(JsonNullable.of("Contrato Atualizado"))
					.build();

			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.update(pathId, dto));

			assertTrue(ex.getMessage().contains("é diferente do ID do corpo"));
			verify(repository, never()).findById(any());
			verify(repository, never()).save(any());
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando o documento não for encontrado")
		void shouldThrowResourceNotFoundExceptionWhenDocumentDoesNotExist() {
			// GIVEN
			UUID id = UUID.randomUUID();

			DocumentUpdateRequestDTO dto = DocumentUpdateRequestDTO.builder()
					.id(id)
					.title(JsonNullable.of("Contrato Atualizado"))
					.build();

			when(repository.findById(id)).thenReturn(Optional.empty());

			// WHEN & THEN
			ResourceNotFoundException ex = assertThrows(
					ResourceNotFoundException.class,
					() -> service.update(id, dto));

			assertTrue(ex.getMessage().contains("Documento não encontrado"));
			verify(repository, times(1)).findById(id);
			verify(repository, never()).save(any());
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro de banco ao buscar ou salvar atualização")
		void shouldThrowDatabaseExceptionWhenUpdateFailsWithDataAccessException() {
			// GIVEN
			UUID id = UUID.randomUUID();

			DocumentUpdateRequestDTO dto = DocumentUpdateRequestDTO.builder()
					.id(id)
					.title(JsonNullable.of("Contrato Atualizado"))
					.build();

			Document existing = Document.builder()
					.id(id)
					.title("Contrato Antigo")
					.build();

			when(repository.findById(id)).thenReturn(Optional.of(existing));
			when(repository.save(any(Document.class)))
					.thenThrow(new DataIntegrityViolationException("Erro de banco"));

			// WHEN & THEN
			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.update(id, dto));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao atualizar documento"));
			verify(repository, times(1)).findById(id);
			verify(repository, times(1)).save(existing);
		}
	}

	@Nested
	@DisplayName("DELETE - Exclusão de documentos")
	class DeleteTests {

		@Test
		@DisplayName("Deve excluir o documento com sucesso quando o ID existir")
		void shouldDeleteDocumentSuccessfully() {
			// GIVEN
			UUID id = UUID.randomUUID();

			when(repository.existsById(id)).thenReturn(true);
			doNothing().when(repository).deleteById(id);

			// WHEN
			service.delete(id);

			// THEN
			verify(repository, times(1)).existsById(id);
			verify(repository, times(1)).deleteById(id);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando o ID for nulo")
		void shouldThrowBadRequestExceptionWhenIdIsNull() {
			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.delete(null));

			assertEquals("ID do documento é obrigatório para exclusão", ex.getMessage());

			verify(repository, never()).existsById(any());
			verify(repository, never()).deleteById(any());
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando o documento não existir")
		void shouldThrowResourceNotFoundExceptionWhenDocumentDoesNotExist() {
			// GIVEN
			UUID id = UUID.randomUUID();

			when(repository.existsById(id)).thenReturn(false);

			// WHEN & THEN
			ResourceNotFoundException ex = assertThrows(
					ResourceNotFoundException.class,
					() -> service.delete(id));

			assertTrue(ex.getMessage().contains("Documento não encontrado"));

			verify(repository, times(1)).existsById(id);
			verify(repository, never()).deleteById(any());
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro ao excluir no banco")
		void shouldThrowDatabaseExceptionWhenDeleteFails() {
			// GIVEN
			UUID id = UUID.randomUUID();

			when(repository.existsById(id)).thenReturn(true);
			doThrow(new DataIntegrityViolationException("Erro"))
					.when(repository).deleteById(id);

			// WHEN & THEN
			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.delete(id));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao excluir documento"));

			verify(repository, times(1)).existsById(id);
			verify(repository, times(1)).deleteById(id);
		}
	}

	@Nested
	@DisplayName("FIND BY ID - Consulta de documentos")
	class FindByIdTests {

		@Test
		@DisplayName("Deve retornar o documento quando o ID existir")
		void shouldReturnDocumentWhenIdExists() {
			// GIVEN
			UUID id = UUID.randomUUID();

			Document entity = Document.builder()
					.id(id)
					.title("Contrato Teste")
					.build();

			when(repository.findById(id)).thenReturn(Optional.of(entity));

			// WHEN
			var response = service.findById(id);

			// THEN
			assertNotNull(response);
			assertEquals(id, response.getId());
			assertEquals("Contrato Teste", response.getTitle());

			verify(repository, times(1)).findById(id);
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando o ID for nulo")
		void shouldThrowBadRequestExceptionWhenIdIsNull() {
			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.findById(null));

			assertEquals("ID do documento é obrigatório para consulta", ex.getMessage());

			verify(repository, never()).findById(any());
		}

		@Test
		@DisplayName("Deve lançar ResourceNotFoundException quando o documento não for encontrado")
		void shouldThrowResourceNotFoundExceptionWhenDocumentDoesNotExist() {
			// GIVEN
			UUID id = UUID.randomUUID();

			when(repository.findById(id)).thenReturn(Optional.empty());

			// WHEN & THEN
			ResourceNotFoundException ex = assertThrows(
					ResourceNotFoundException.class,
					() -> service.findById(id));

			assertTrue(ex.getMessage().contains("Documento não encontrado"));

			verify(repository, times(1)).findById(id);
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro ao buscar no banco")
		void shouldThrowDatabaseExceptionWhenFindFails() {
			// GIVEN
			UUID id = UUID.randomUUID();

			when(repository.findById(id))
					.thenThrow(new DataAccessException("Erro") {
					});

			// WHEN & THEN
			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.findById(id));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao buscar documento"));

			verify(repository, times(1)).findById(id);
		}
	}

	@Nested
	@DisplayName("SEARCH - Busca de documentos com filtros dinâmicos")
	class SearchTests {

		@Test
		@DisplayName("Deve buscar documentos usando CONTAINS quando searchType não for STARTS_WITH")
		void shouldSearchUsingContains() {
			// GIVEN
			DocumentSearchDTO dto = DocumentSearchDTO.builder()
					.title("Contrato")
					.searchType(SearchTypeEnum.CONTAINS)
					.build();

			Pageable pageable = PageRequest.of(0, 10);

			Page<Document> page = new PageImpl<>(List.of(
					Document.builder().id(UUID.randomUUID()).title("Contrato A").build()));

			when(repository.findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable))).thenReturn(page);

			// WHEN
			var result = service.search(dto, pageable);

			// THEN
			assertNotNull(result);
			assertEquals(1, result.getTotalElements());

			verify(repository, times(1)).findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable));
		}

		@Test
		@DisplayName("Deve buscar documentos usando STARTS_WITH quando configurado")
		void shouldSearchUsingStartsWith() {
			// GIVEN
			DocumentSearchDTO dto = DocumentSearchDTO.builder()
					.title("Con")
					.searchType(SearchTypeEnum.STARTS_WITH)
					.build();

			Pageable pageable = PageRequest.of(0, 10);

			Page<Document> page = new PageImpl<>(List.of(
					Document.builder().id(UUID.randomUUID()).title("Contrato X").build()));

			when(repository.findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable))).thenReturn(page);

			// WHEN
			var result = service.search(dto, pageable);

			// THEN
			assertNotNull(result);
			assertEquals(1, result.getTotalElements());

			verify(repository, times(1)).findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable));
		}

		@Test
		@DisplayName("Deve buscar documentos aplicando múltiplos filtros (status, owner e tags)")
		void shouldSearchWithMultipleFilters() {
			// GIVEN
			DocumentSearchDTO dto = DocumentSearchDTO.builder()
					.title("Contrato")
					.searchType(SearchTypeEnum.CONTAINS)
					.status(DocumentStatusEnum.PUBLISHED)
					.owner("user1")
					.tags(List.of("financeiro"))
					.build();

			Pageable pageable = PageRequest.of(0, 10);

			Page<Document> page = new PageImpl<>(List.of(
					Document.builder().id(UUID.randomUUID()).title("Contrato Financeiro").build()));

			when(repository.findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable))).thenReturn(page);

			// WHEN
			var result = service.search(dto, pageable);

			// THEN
			assertNotNull(result);
			assertEquals(1, result.getTotalElements());

			verify(repository, times(1)).findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable));
		}

		@Test
		@DisplayName("Deve retornar lista vazia quando nenhum documento for encontrado")
		void shouldReturnEmptyWhenNoResults() {
			// GIVEN
			DocumentSearchDTO dto = DocumentSearchDTO.builder()
					.title("Inexistente")
					.build();

			Pageable pageable = PageRequest.of(0, 10);

			Page<Document> emptyPage = Page.empty();

			when(repository.findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable))).thenReturn(emptyPage);

			// WHEN
			var result = service.search(dto, pageable);

			// THEN
			assertNotNull(result);
			assertEquals(0, result.getTotalElements());

			verify(repository, times(1)).findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable));
		}

		@Test
		@DisplayName("Deve lançar BadRequestException quando o DTO for nulo")
		void shouldThrowBadRequestExceptionWhenDtoIsNull() {
			// GIVEN
			Pageable pageable = PageRequest.of(0, 10);

			// WHEN & THEN
			BadRequestException ex = assertThrows(
					BadRequestException.class,
					() -> service.search(null, pageable));

			assertEquals("Parâmetros de busca não podem ser nulos", ex.getMessage());

			verify(repository, never()).findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable));
		}

		@Test
		@DisplayName("Deve lançar DatabaseException quando ocorrer erro ao buscar no banco")
		void shouldThrowDatabaseExceptionWhenSearchFails() {
			// GIVEN
			DocumentSearchDTO dto = DocumentSearchDTO.builder()
					.title("Contrato")
					.build();

			Pageable pageable = PageRequest.of(0, 10);

			when(repository.findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable))).thenThrow(new DataAccessException("Erro") {
					});

			// WHEN & THEN
			DatabaseException ex = assertThrows(
					DatabaseException.class,
					() -> service.search(dto, pageable));

			assertTrue(ex.getMessage().contains("Erro de banco de dados ao buscar documentos"));

			verify(repository, times(1)).findAll(
					ArgumentMatchers.<Specification<Document>>any(),
					eq(pageable));
		}
	}
}

package com.ged.backend.service.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.ged.backend.exception.StorageException;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;

@ExtendWith(MockitoExtension.class)
class MinIOStorageServiceTest {

	@Mock
	private MinioClient minioClient;

	@InjectMocks
	private MinIOStorageService service;

	private final String bucket = "documents";

	@BeforeEach
	void setup() throws Exception {
		// Injeta o @Value manualmente
		Field field = MinIOStorageService.class.getDeclaredField("bucket");
		field.setAccessible(true);
		field.set(service, bucket);
	}

	@Nested
	@DisplayName("UPLOAD - MinIO")
	class UploadTests {

		@Test
		@DisplayName("Deve realizar upload com sucesso")
		void shouldUploadSuccessfully() throws Exception {
			// GIVEN
			MultipartFile file = mock(MultipartFile.class);

			when(file.getOriginalFilename()).thenReturn("file.pdf");
			when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
			when(file.getSize()).thenReturn(4L);
			when(file.getContentType()).thenReturn("application/pdf");

			// WHEN
			String result = service.upload(file);

			// THEN
			assertNotNull(result);
			assertTrue(result.contains("file.pdf"));

			verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
		}

		@Test
		@DisplayName("Deve lançar StorageException quando MinIO falhar")
		void shouldThrowStorageExceptionWhenMinioFails() throws Exception {
			// GIVEN
			MultipartFile file = mock(MultipartFile.class);

			when(file.getOriginalFilename()).thenReturn("file.pdf");
			when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
			when(file.getSize()).thenReturn(4L);
			when(file.getContentType()).thenReturn("application/pdf");

			doThrow(new RuntimeException("erro"))
					.when(minioClient)
					.putObject(any(PutObjectArgs.class));

			// WHEN & THEN
			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.upload(file));

			assertTrue(ex.getMessage().contains("Erro ao realizar upload"));
		}

		@Test
		@DisplayName("Deve lançar StorageException quando falhar leitura do arquivo")
		void shouldThrowStorageExceptionWhenInputStreamFails() throws Exception {
			// GIVEN
			MultipartFile file = mock(MultipartFile.class);

			when(file.getOriginalFilename()).thenReturn("file.pdf");
			when(file.getInputStream()).thenThrow(new RuntimeException("erro leitura"));

			// WHEN & THEN
			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.upload(file));

			assertTrue(ex.getMessage().contains("Erro ao realizar upload"));
		}

		@Test
		@DisplayName("Deve gerar storageKey contendo nome original do arquivo")
		void shouldGenerateStorageKeyWithOriginalFileName() throws Exception {
			// GIVEN
			MultipartFile file = mock(MultipartFile.class);

			when(file.getOriginalFilename()).thenReturn("documento.pdf");
			when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
			when(file.getSize()).thenReturn(4L);
			when(file.getContentType()).thenReturn("application/pdf");

			// WHEN
			String result = service.upload(file);

			// THEN
			assertNotNull(result);
			assertTrue(result.contains("documento.pdf"));
		}
	}

	@Nested
	@DisplayName("DOWNLOAD - MinIO")
	class DownloadTests {

		@Test
		@DisplayName("Deve realizar download com sucesso")
		void shouldDownloadSuccessfully() throws Exception {
			// GIVEN
			String key = "file-key";

			GetObjectResponse mockResponse = mock(GetObjectResponse.class);

			when(minioClient.getObject(any(GetObjectArgs.class)))
					.thenReturn(mockResponse);

			// WHEN
			InputStream result = service.download(key);

			// THEN
			assertNotNull(result);
			verify(minioClient, times(1))
					.getObject(any(GetObjectArgs.class));
		}

		@Test
		@DisplayName("Deve lançar StorageException quando arquivo não existir (NoSuchKey)")
		void shouldThrowStorageExceptionWhenFileNotFound() throws Exception {
			// GIVEN
			String key = "file-key";

			ErrorResponseException minioException = mock(ErrorResponseException.class);
			ErrorResponse errorResponse = mock(ErrorResponse.class);

			when(minioException.errorResponse()).thenReturn(errorResponse);
			when(errorResponse.code()).thenReturn("NoSuchKey");

			when(minioClient.getObject(any(GetObjectArgs.class)))
					.thenThrow(minioException);

			// WHEN & THEN
			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.download(key));

			assertEquals("Arquivo não encontrado no storage", ex.getMessage());
		}

		@Test
		@DisplayName("Deve lançar StorageException para erro genérico do MinIO")
		void shouldThrowStorageExceptionForGenericMinioError() throws Exception {
			// GIVEN
			String key = "file-key";

			ErrorResponseException minioException = mock(ErrorResponseException.class);
			ErrorResponse errorResponse = mock(ErrorResponse.class);

			when(minioException.errorResponse()).thenReturn(errorResponse);
			when(errorResponse.code()).thenReturn("InternalError");

			when(minioClient.getObject(any(GetObjectArgs.class)))
					.thenThrow(minioException);

			// WHEN & THEN
			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.download(key));

			assertTrue(ex.getMessage().contains("Erro ao realizar download"));
		}

		@Test
		@DisplayName("Deve lançar StorageException para erro inesperado")
		void shouldThrowStorageExceptionForUnexpectedError() throws Exception {
			// GIVEN
			String key = "file-key";

			when(minioClient.getObject(any(GetObjectArgs.class)))
					.thenThrow(new RuntimeException("erro"));

			// WHEN & THEN
			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.download(key));

			assertTrue(ex.getMessage().contains("Erro ao realizar download"));
		}
	}

	@Nested
	@DisplayName("DELETE - MinIO")
	class DeleteTests {

		@Test
		@DisplayName("Deve excluir arquivo com sucesso")
		void shouldDeleteSuccessfully() throws Exception {
			// GIVEN
			String key = "file-key";

			// WHEN
			service.delete(key);

			// THEN
			verify(minioClient, times(1))
					.removeObject(any(RemoveObjectArgs.class));
		}

		@Test
		@DisplayName("Deve lançar StorageException quando MinIO falhar")
		void shouldThrowStorageExceptionWhenDeleteFails() throws Exception {
			// GIVEN
			String key = "file-key";

			doThrow(new RuntimeException("erro"))
					.when(minioClient)
					.removeObject(any(RemoveObjectArgs.class));

			// WHEN & THEN
			StorageException ex = assertThrows(
					StorageException.class,
					() -> service.delete(key));

			assertTrue(ex.getMessage().contains("Erro ao excluir arquivo"));
		}
	}
}

package com.ged.backend.service.storage;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ged.backend.exception.StorageException;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinIOStorageService implements StorageService {

	private final MinioClient minioClient;

	@Value("${MINIO_BUCKET}")
	private String bucket;

	@Override
	public String upload(MultipartFile file) {
		log.info("Upload iniciado. Nome: {}", file.getOriginalFilename());

		try {
			String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();

			minioClient.putObject(
					PutObjectArgs.builder()
							.bucket(bucket)
							.object(objectName)
							.stream(file.getInputStream(), file.getSize(), -1)
							.contentType(file.getContentType())
							.build());

			log.info("Upload realizado com sucesso. Key: {}", objectName);

			return objectName;

		} catch (Exception ex) {
			String msg = String.format("Erro ao realizar upload no MinIO. File: %s",
					file.getOriginalFilename());
			log.error(msg, ex);
			throw new StorageException(msg, ex);
		}
	}

	@Override
	public InputStream download(String storageKey) {
		log.info("Download iniciado. Key: {}", storageKey);

		try {
			return minioClient.getObject(
					GetObjectArgs.builder()
							.bucket(bucket)
							.object(storageKey)
							.build());

		} catch (ErrorResponseException ex) {
			if (ex.errorResponse().code().equals("NoSuchKey")) {
				throw new StorageException("Arquivo não encontrado no storage");
			}

			log.error("Erro MinIO", ex);
			throw new StorageException("Erro ao realizar download no storage", ex);

		} catch (Exception ex) {
			log.error("Erro inesperado no download", ex);
			throw new StorageException("Erro ao realizar download no storage", ex);
		}
	}

	@Override
	public void delete(String storageKey) {
		log.info("Exclusão iniciada. Key: {}", storageKey);

		try {
			minioClient.removeObject(
					RemoveObjectArgs.builder()
							.bucket(bucket)
							.object(storageKey)
							.build());

			log.info("Arquivo removido com sucesso. Key: {}", storageKey);

		} catch (Exception ex) {
			String msg = String.format("Erro ao excluir arquivo no storage. Key: %s", storageKey);
			log.error(msg, ex);
			throw new StorageException(msg, ex);
		}
	}
}
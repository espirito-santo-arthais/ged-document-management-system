package com.ged.backend.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
		name = "document_versions",
		indexes = {
				@Index(name = "document_versions_idx_document", columnList = "document_id"),
				@Index(name = "document_versions_idx_version", columnList = "version"),
				@Index(name = "document_versions_idx_storage_key", columnList = "storage_key")
		},
		uniqueConstraints = {
				@UniqueConstraint(name = "document_versions_uk_document_version", columnNames = { "document_id", "version" })
		})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "document")
public class DocumentVersion {

	/**
	 * Identificador único da versão do documento
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@EqualsAndHashCode.Include
	private UUID id;

	/**
	 * Documento ao qual esta versão pertence
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "document_id", nullable = false, foreignKey = @ForeignKey(name = "document_versions_fk_document"))
	@NotNull(message = "Documento é obrigatório")
	private Document document;

	/**
	 * Número da versão do documento (incremental)
	 */
	@Column(nullable = false)
	@NotNull(message = "Versão é obrigatória")
	@Min(value = 1, message = "Versão deve ser maior ou igual a 1")
	private Integer version;

	/**
	 * Nome original do arquivo enviado
	 */
	@Column(name = "file_name", nullable = false, length = 255)
	@NotBlank(message = "Nome do arquivo é obrigatório")
	@Size(max = 255, message = "Nome do arquivo deve ter no máximo 255 caracteres")
	private String fileName;

	/**
	 * Tipo do conteúdo (MIME type)
	 */
	@Column(name = "content_type", nullable = false, length = 100)
	@NotBlank(message = "Tipo de conteúdo é obrigatório")
	@Size(max = 100, message = "Tipo de conteúdo deve ter no máximo 100 caracteres")
	@Pattern(regexp = "^[a-zA-Z0-9!#$&^_.+-]+/[a-zA-Z0-9!#$&^_.+-]+$", message = "Tipo de conteúdo inválido")
	private String contentType;

	/**
	 * Tamanho do arquivo em bytes
	 */
	@Column(nullable = false)
	@NotNull(message = "Tamanho é obrigatório")
	@Positive(message = "Tamanho deve ser maior que zero")
	private Long size;

	/**
	 * Identificador do arquivo no storage (MinIO, S3, etc)
	 */
	@Column(name = "storage_key", nullable = false, length = 500)
	@NotBlank(message = "Chave de armazenamento é obrigatória")
	@Size(max = 500, message = "Chave de armazenamento deve ter no máximo 500 caracteres")
	private String storageKey;

	/**
	 * Data de criação da versão
	 */
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
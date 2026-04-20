package com.ged.backend.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ged.backend.domain.enums.DocumentStatusEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Entity
@Table(
		name = "documents",
		indexes = {
				@Index(name = "documents_idx_title", columnList = "title"),
				@Index(name = "documents_idx_status", columnList = "status"),
				@Index(name = "documents_idx_owner", columnList = "owner") })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "tags")
public class Document {

	/**
	 * Identificador único do documento
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@EqualsAndHashCode.Include
	private UUID id;

	/**
	 * Título do documento
	 */
	@Column(nullable = false, length = 255)
	@NotBlank(message = "Título é obrigatório")
	@Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
	private String title;

	/**
	 * Descrição detalhada do documento
	 */
	@Column(name = "description", length = 1000)
	@Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
	private String description;

	/**
	 * Identificação do proprietário do documento
	 */
	@Column(nullable = false, length = 100)
	@NotBlank(message = "Proprietário é obrigatório")
	@Size(max = 100, message = "Proprietário deve ter no máximo 100 caracteres")
	private String owner;

	/**
	 * Lista de tags associadas ao documento
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "document_tags", joinColumns = @JoinColumn(name = "document_id"))
	@Column(name = "tag")
	@Size(max = 10, message = "Máximo de 10 tags permitidas")
	@Singular(value = "tag")
	private List<@NotBlank(message = "Tag não pode estar em branco") @Size(max = 50, message = "Tag deve ter no máximo 50 caracteres") String> tags;

	/**
	 * Status atual do documento (DRAFT, PUBLISHED, ARCHIVED)
	 */
	@Column(name = "status", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status é obrigatório")
	private DocumentStatusEnum status;

	/**
	 * Data e hora de criação do documento
	 */
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	/**
	 * Data e hora da última atualização do documento
	 */
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;

		if (this.status == null) {
			this.status = DocumentStatusEnum.DRAFT;
		}
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
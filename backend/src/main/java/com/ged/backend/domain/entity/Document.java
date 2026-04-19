package com.ged.backend.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ged.backend.domain.enums.DocumentStatusEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
		name = "documents", 
		indexes = {
				@Index(name = "idx_document_title", columnList = "title"),
				@Index(name = "idx_document_status", columnList = "status"),
				@Index(name = "idx_document_owner", columnList = "owner")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "tags")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@EqualsAndHashCode.Include
	private UUID id;

	@Column(nullable = false, length = 255)
	@NotBlank(message = "Title is required")
	@Size(max = 255, message = "Title must have at most 255 characters")
	private String title;

	@Column(name = "description", length = 1000)
	@Size(max = 1000, message = "Description must have at most 1000 characters")
	private String description;

	@Column(nullable = false, length = 100)
	@NotBlank(message = "Owner is required")
	@Size(max = 100, message = "Owner must have at most 100 characters")
	private String owner;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "document_tags", joinColumns = @JoinColumn(name = "document_id"))
	@Column(name = "tag")
	@Size(max = 10, message = "Maximum 10 tags allowed")
	@Singular(value = "tag")
	private List<
		@NotBlank(message = "Tag cannot be blank") 
		@Size(max = 50, message = "Tag must have at most 50 characters") 
		String> tags;

	@Column(name = "status", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status is required")
	private DocumentStatusEnum status;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

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
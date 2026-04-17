package com.ged.backend.domain.dto.document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ged.backend.domain.enums.DocumentStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "DocumentoResponse", description = "DTO de resposta do documento")
public class DocumentResponseDTO {

	private UUID id;

	private String title;
	private String description;
	private String owner;

	private List<String> tags;

	private DocumentStatusEnum status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}

package com.ged.backend.domain.dto.document;

import java.time.LocalDateTime;
import java.util.List;

import com.ged.backend.domain.enums.DocumentStatusEnum;
import com.ged.backend.domain.enums.SearchTypeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(name = "DocumentSearchDTO", description = "Parâmetros de busca de documentos")
public class DocumentSearchDTO {

	@Schema(description = "Texto para busca em título ou descrição", example = "contrato")
	@Size(max = 255, message = "Title filter must have at most 255 characters")
	private String title;

	@Schema(description = "Identificador do proprietário do documento", example = "user123")
	@Size(max = 100, message = "Owner must have at most 100 characters")
	private String owner;

	@Schema(description = "Status do documento", example = "PUBLISHED")
	private DocumentStatusEnum status;

	@Schema(description = "Data mínima de criação (inclusive)", example = "2024-01-01T00:00:00")
	private LocalDateTime createdAfter;

	@Schema(description = "Data máxima de criação (inclusive)", example = "2024-12-31T23:59:59")
	private LocalDateTime createdBefore;

	@Schema(description = "Data mínima de atualização (inclusive)", example = "2024-01-01T00:00:00")
	private LocalDateTime updatedAfter;

	@Schema(description = "Data máxima de atualização (inclusive)", example = "2024-12-31T23:59:59")
	private LocalDateTime updatedBefore;

	@Schema(
			description = "Lista de tags para filtro (retorna documentos que possuem qualquer uma das tags)", 
			example = "[\"financeiro\", \"contrato\"]")
	@Size(max = 10, message = "Maximum 10 tags allowed")
	private List<
		@NotBlank(message = "Tag cannot be blank") 
		@Size(max = 50, message = "Tag must have at most 50 characters") 
		String> tags;	

	@Schema(
			description = "Tipo de busca textual (CONTAINS ou STARTS_WITH)", 
			example = "CONTAINS", 
			defaultValue = "CONTAINS")
	@Builder.Default
	private SearchTypeEnum searchType = SearchTypeEnum.CONTAINS;
}
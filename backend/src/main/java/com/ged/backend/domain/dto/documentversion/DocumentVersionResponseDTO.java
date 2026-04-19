package com.ged.backend.domain.dto.documentversion;

import java.time.LocalDateTime;
import java.util.UUID;

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
public class DocumentVersionResponseDTO {

	private UUID id;

	private Integer version;

	private String fileName;

	private String contentType;

	private Long size;

	private LocalDateTime createdAt;
}
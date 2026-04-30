package com.ged.backend.domain.result.documentversion;

import java.io.InputStream;

import com.ged.backend.domain.dto.documentversion.DocumentVersionResponseDTO;

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
public class DocumentVersionDownloadResult {

	private InputStream inputStream;

	private DocumentVersionResponseDTO metadata;
}
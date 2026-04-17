package com.ged.backend.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentStatusEnum {

	DRAFT(1, "Draft - Em edição"),
	PUBLISHED(2, "Published - Disponível"),
	ARCHIVED(3, "Archived - Arquivado");

	private final int id;
	private final String description;
}

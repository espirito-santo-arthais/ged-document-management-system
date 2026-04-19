package com.ged.backend.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchTypeEnum {

	CONTAINS(1, "Containing - Contém em qualquer parte"),
	STARTS_WITH(2, "Starts With - Começa com");

	private final int id;
	private final String description;

	public static SearchTypeEnum fromString(String value) {
		if (value == null || value.isBlank()) {
			return CONTAINS; // padrão
		}

		try {
			return SearchTypeEnum.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return CONTAINS;
		}
	}

	public static SearchTypeEnum fromId(Integer id) {
		if (id == null) {
			return CONTAINS;
		}

		for (SearchTypeEnum type : values()) {
			if (type.getId() == id) {
				return type;
			}
		}

		return CONTAINS;
	}
}
package com.ged.backend.domain.dto.document;

import java.time.LocalDateTime;
import java.util.List;

import com.ged.backend.domain.enums.DocumentStatusEnum;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DocumentSearchDTO {

    /**
     * Busca por título (prefixo ou containing depende do endpoint/service)
     */
    @Size(max = 255, message = "Title filter must have at most 255 characters")
    private String title;

    /**
     * Dono do documento
     */
    @Size(max = 100, message = "Owner must have at most 100 characters")
    private String owner;

    /**
     * Status do documento
     */
    private DocumentStatusEnum status;

    /**
     * Filtro por período de criação
     */
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    /**
     * Filtro por período de atualização
     */
    private LocalDateTime updatedAfter;
    private LocalDateTime updatedBefore;

    /**
     * Filtro por tags (qualquer uma delas)
     */
    @Size(max = 10, message = "Maximum 10 tags allowed")
    private List<
        @Size(max = 50, message = "Tag must have at most 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9-_]*$", message = "Tag must be alphanumeric")
        String
    > tags;

    /**
     * Define se a busca será por prefixo (true) ou containing (false)
     */
    @Builder.Default
    private Boolean startsWith = false;
}
package com.ged.backend.domain.dto.document;

import java.util.List;
import java.util.UUID;

import org.openapitools.jackson.nullable.JsonNullable;

import com.ged.backend.domain.enums.DocumentStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(
    name = "DocumentoAtualizacaoRequest",
    description = "DTO utilizado para atualização parcial (PATCH) de um documento. " +
                  "Campos não enviados não serão alterados. Campos enviados como null serão limpos."
)
public class DocumentUpdateRequestDTO {

    @Schema(
        description = "Identificador único do documento",
        example = "550e8400-e29b-41d4-a716-446655440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "O id é obrigatório")
    private UUID id;

    @Builder.Default
    @Schema(
        description = "Título do documento. " +
                      "Se não informado, não será alterado. " +
                      "Se enviado como null, lançará uma exceção.",
        example = "Novo título do documento",
        maxLength = 255,
        nullable = true
    )
    private JsonNullable<
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String
    > title = JsonNullable.undefined();

    @Builder.Default
    @Schema(
        description = "Descrição detalhada do documento. " +
                      "Se não informado, não será alterado. " +
                      "Se enviado como null, será removido.",
        example = "Nova descrição do documento",
        maxLength = 1000,
        nullable = true
    )
    private JsonNullable<
        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
        String
    > description = JsonNullable.undefined();

    @Builder.Default
    @Schema(
        description = "Responsável pelo documento. " +
                    "Se não informado, não será alterado. " +
                    "Se enviado como null, lançará uma exceção.",
        example = "admin",
        maxLength = 100,
        nullable = true
    )
    private JsonNullable<
        @Size(max = 100, message = "O responsável deve ter no máximo 100 caracteres")
        String
    > owner = JsonNullable.undefined();

    @Builder.Default
    @Schema(
        description = "Status do documento. " +
                    "Se não informado, não será alterado. " +
                    "Se enviado como null, lançará uma exceção.",
        example = "PUBLISHED",
        nullable = true
    )
    private JsonNullable<DocumentStatusEnum>
        status = JsonNullable.undefined();

    @Builder.Default
    @Schema(
        description = "Lista de tags do documento (máximo de 10 itens). " +
                      "Se não informado, não será alterado. " +
                      "Se enviado como null, remove todas as tags.",
        example = "[\"financeiro\", \"contrato\"]",
        nullable = true
    )
    private JsonNullable<
        @Size(max = 10, message = "É permitido no máximo 10 tags")
        List<
            @NotBlank(message = "A tag não pode estar em branco")
            @Size(max = 50, message = "A tag deve ter no máximo 50 caracteres")
            String
        >
    > tags = JsonNullable.undefined();
}
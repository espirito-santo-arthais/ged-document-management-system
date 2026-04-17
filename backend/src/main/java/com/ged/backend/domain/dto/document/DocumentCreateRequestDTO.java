package com.ged.backend.domain.dto.document;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "DocumentoCriacaoRequest", description = "DTO utilizado para criar um novo documento")
public class DocumentCreateRequestDTO {

    @Schema(
        description = "Título do documento",
        example = "Contrato de Prestação de Serviços",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 255
    )
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
    private String title;

    @Schema(
        description = "Descrição detalhada do documento",
        example = "Contrato firmado entre as partes para prestação de serviços",
        maxLength = 1000
    )
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String description;

    @Schema(
        description = "Responsável pelo documento",
        example = "admin",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "O responsável é obrigatório")
    @Size(max = 100, message = "O responsável deve ter no máximo 100 caracteres")
    private String owner;

    @Schema(
    	    description = "Lista de tags associadas ao documento (máximo de 10 itens, cada tag com até 50 caracteres e não pode estar em branco)",
    	    example = "[\"financeiro\", \"contrato\"]"
    )
    @Size(max = 10, message = "É permitido no máximo 10 tags")
    @Singular("tag")
    private List<
        @NotBlank(message = "A tag não pode estar em branco")
        @Size(max = 50, message = "A tag deve ter no máximo 50 caracteres")
        String> tags;
}
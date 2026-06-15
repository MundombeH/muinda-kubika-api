package com.api.muinda_kubika.DTO.Files.AnalizeIA;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoIAGitUrlRequestDto {

    @NotBlank(message = "A URL do repositório é obrigatória")
    private String gitUrl;
}

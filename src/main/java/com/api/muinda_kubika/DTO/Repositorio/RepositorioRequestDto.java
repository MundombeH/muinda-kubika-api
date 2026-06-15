package com.api.muinda_kubika.DTO.Repositorio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class RepositorioRequestDto {

    @NotNull(message = "Documento não pode ser vazio")
    private UUID documento;

    @NotBlank(message = "url do projecto é obrigatoria")
    @URL(message = "Formato da url incorrecto")
    private String urlGithub;

    @Size(min = 1, message = "Tem de ser selecionada pelo menos uma tecnologia")
    private Set<String> tecnologiasUsadas;
}

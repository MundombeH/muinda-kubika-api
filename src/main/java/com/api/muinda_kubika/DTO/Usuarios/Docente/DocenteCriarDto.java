package com.api.muinda_kubika.DTO.Usuarios.Docente;

import com.api.muinda_kubika.Enums.GeneroEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class DocenteCriarDto {

    @NotBlank(message = "A identificação é obrigatória")
    private String identificacao;

    private String departamento;

    private GeneroEnum genero;

    private Set<UUID> instituicoes = new HashSet<>();
}

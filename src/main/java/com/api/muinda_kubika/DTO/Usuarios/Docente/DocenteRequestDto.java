package com.api.muinda_kubika.DTO.Usuarios.Docente;

import com.api.muinda_kubika.Enums.GeneroEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class DocenteRequestDto {

    @NotNull(message = "O  departamento não pode ser vazio")
    private  String departamento;
    @NotNull(message = "A identificação não pode ser vazia")
    private  String identificacao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O Genero não pode ser vazio")
    private GeneroEnum genero;

    @NotNull(message = "O Bairro não pode ser vazio")
    private UUID bairro;

    private Set<UUID> instituicoes = new HashSet<>();
}

package com.api.muinda_kubika.DTO.Usuarios.Estudante;

import com.api.muinda_kubika.Enums.GeneroEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class EstudanteCriarDto {

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    @Positive(message = "O ano academico não pode ser negativo")
    private Integer ano;

    @NotNull(message = "O genero é obrigatório")
    private GeneroEnum genero;

    private String identificacao;

    @NotNull(message = "A instituição é obrigatória")
    private UUID instituicaoId;
}

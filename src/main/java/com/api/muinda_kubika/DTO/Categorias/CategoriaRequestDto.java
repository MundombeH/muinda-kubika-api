package com.api.muinda_kubika.DTO.Categorias;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoriaRequestDto {

    @NotEmpty(message = "descricao é obrigatoria")
    private String descricao;
}

package com.api.muinda_kubika.DTO.Localizacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaisesRequestDTO {

//    private UUID id;

    @NotBlank(message = "A descricao do pais nao pode estar em branco")
    @NotNull(message = "A descricao do pais nao pode ser nula")
    private String descricao;
}

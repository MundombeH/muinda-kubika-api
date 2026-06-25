package com.api.muinda_kubika.DTO.Localizacao;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProvinciasRequestDTO {

//    private UUID id;
    @NotBlank
    @NotNull
    private String descricao;

    @NotNull
    private UUID paisId;
}

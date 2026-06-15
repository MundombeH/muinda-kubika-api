package com.api.muinda_kubika.DTO.Files.Ficheiros;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
public class FicheirosRequestDto {

    private String nome;

    @NotNull(message = "O id do documento é obrigatório")
    private UUID documento;
}

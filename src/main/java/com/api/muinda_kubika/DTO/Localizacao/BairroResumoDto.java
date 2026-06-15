package com.api.muinda_kubika.DTO.Localizacao;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class BairroResumoDto {
    private UUID id;
    private String descricao;
    private Boolean isActive;
    private UUID municipioId;
}

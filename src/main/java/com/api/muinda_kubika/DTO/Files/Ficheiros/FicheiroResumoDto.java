package com.api.muinda_kubika.DTO.Files.Ficheiros;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class FicheiroResumoDto {
    private UUID id;
    private String url;
    private String nome;
    private String formato;
}

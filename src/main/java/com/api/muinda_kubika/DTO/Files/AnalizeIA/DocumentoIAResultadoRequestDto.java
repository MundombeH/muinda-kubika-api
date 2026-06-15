package com.api.muinda_kubika.DTO.Files.AnalizeIA;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoIAResultadoRequestDto {

    @NotNull(message = "O Documento é obrigatorio")
    private UUID documentoId;

    private String titulo;

    private String resumo;

    private Set<String> autores = new HashSet<>();

    private String categoriaSugerida;

    private Integer categoriaConfianca;

    private String subcategoriaSugerida;

    private Integer subcategoriaConfianca;

    private Set<SugestaoConfiancaDto> palavrasChaveIA = new HashSet<>();

    private Set<SugestaoConfiancaDto> tagsSugeridas = new HashSet<>();

    private Set<SugestaoConfiancaDto> tecnologiasSugeridas = new HashSet<>();

    private Set<SugestaoConfiancaDto> frameworksSugeridos = new HashSet<>();

    private Set<String> conflitosDetectados = new HashSet<>();
}

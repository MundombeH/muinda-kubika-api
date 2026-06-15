package com.api.muinda_kubika.DTO.Files.AnalizeIA;

import com.api.muinda_kubika.model.Files.DocumentosModel;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoAnalizeRequestDto {

    @NotBlank(message = "O Documento é obrigatorio")
    private DocumentosModel documento;

    private String resumoGeradoIA;

    private String tituloSugerido;

    private Integer tituloConfianca;

    private String categoriaSugerida;

    private Integer categoriaConfianca;

    private String subcategoriaSugerida;

    private Integer subcategoriaConfianca;

    private Set<SugestaoConfiancaDto> palavrasChaveIA = new HashSet<>();

    private Set<SugestaoConfiancaDto> tagsSugeridas = new HashSet<>();

    private Set<SugestaoConfiancaDto> tecnologiasSugeridas = new HashSet<>();

    private Set<SugestaoConfiancaDto> frameworksSugeridos = new HashSet<>();

    private Set<String> conflitosDetectados = new HashSet<>();

    private String motivoRejeicao;

    private String observacaoAdmin;

    private LocalDateTime dataProcessamento;

    private Integer versao;
}

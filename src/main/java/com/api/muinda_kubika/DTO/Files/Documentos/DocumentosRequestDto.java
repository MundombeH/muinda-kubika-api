package com.api.muinda_kubika.DTO.Files.Documentos;

import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class DocumentosRequestDto {

    private String titulo;

    private String resumo;

    private Set<String> autores = new HashSet<>();

    private UUID instituicao;

    @NotNull(message = "Tipo de documento é obrigatorio")
    private TipoDocumentoEnum tipoDeDocumento;

    private Integer versao;

    private Set<UUID> categorias = new HashSet<>();

    private Set<UUID> tags = new HashSet<>();
}

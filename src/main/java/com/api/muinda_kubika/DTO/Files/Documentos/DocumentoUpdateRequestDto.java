package com.api.muinda_kubika.DTO.Files.Documentos;

import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoUpdateRequestDto {
    private String titulo;
    private String resumo;
    private Set<String> autores;
    private TipoDocumentoEnum tipoDeDocumento;
    private StatusDocumentoEnum status;
    private String capaUrl;
    private Set<String> categorias = new HashSet<>();
    private Set<String> tags = new HashSet<>();
}

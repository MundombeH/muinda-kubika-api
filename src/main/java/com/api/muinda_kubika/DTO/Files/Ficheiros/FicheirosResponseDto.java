package com.api.muinda_kubika.DTO.Files.Ficheiros;

import com.api.muinda_kubika.DTO.Files.Documentos.DocumentoResumoDto;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FicheirosResponseDto extends DefaultDto {
    private String nome;
    private String url;
    private Long tamanho;
    private String formato;
    private String mimeType;
    private String checksum;
    private DocumentoResumoDto documento;
}

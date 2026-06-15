package com.api.muinda_kubika.DTO.Files.Documentos;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoResumoDto {

    private String titulo;
    private String resumo;
    private Set<String> autores = new HashSet<>();
    private DefaultUserResumoDto usuario;
    private InstituicoesResumoDto instituicao;
    private Integer versao;
    private TipoDocumentoEnum tipoDocumento;
    private StatusDocumentoEnum status;
}

package com.api.muinda_kubika.DTO.Files.Documentos;

import com.api.muinda_kubika.DTO.Categorias.CategroiaRepsonseDto;
import com.api.muinda_kubika.DTO.Files.Ficheiros.FicheiroResumoDto;
import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Tags.TagsResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Defaults.DefaultDto;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentosResponseDto extends DefaultDto {

    private String titulo;
    private String resumo;

    private Set<String> autores = new HashSet<>();

    private DefaultUserResumoDto usuario;

    private InstituicoesResumoDto instituicao;

    @Enumerated(EnumType.STRING)
    private TipoDocumentoEnum tipoDeDocumento;

    @Enumerated(EnumType.STRING)
    private StatusDocumentoEnum status;

    private DefaultUserResumoDto aprovadoPor;

    private Integer versao;

    private Set<CategroiaRepsonseDto> categorias = new HashSet<>();

    private Set<TagsResponseDto> tags = new HashSet<>();

    private Set<FicheiroResumoDto> ficheiros = new HashSet<>();
}

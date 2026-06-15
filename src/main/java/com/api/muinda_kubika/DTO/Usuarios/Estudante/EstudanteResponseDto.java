package com.api.muinda_kubika.DTO.Usuarios.Estudante;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResponseDto;
import com.api.muinda_kubika.DTO.Localizacao.BairroResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.GeneroEnum;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstudanteResponseDto extends DefaultDto {

    private String curso;
    private Integer ano;

    private GeneroEnum genero;

    private String identificacao;

    private BairroResumoDto bairro;

    private DefaultUserResumoDto usuario;
    private InstituicoesResponseDto instituicao;


}

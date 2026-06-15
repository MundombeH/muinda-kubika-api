package com.api.muinda_kubika.DTO.Usuarios.Docente;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Localizacao.BairroResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.GeneroEnum;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class DocenteResponseDto extends DefaultDto {
    private  String identificacao;

    private GeneroEnum genero;

    private BairroResumoDto bairro;

    private DefaultUserResumoDto usuario;

    private Set<InstituicoesResumoDto> instituicoes = new HashSet<>();
}

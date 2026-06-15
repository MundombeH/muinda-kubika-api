package com.api.muinda_kubika.DTO.Usuarios.AdminInstituicao;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class AdminInstituicaoReponseDto extends DefaultDto {

    private DefaultUserResumoDto usuario;
    private Set<InstituicoesResumoDto> instituicoes = new HashSet<>();
}

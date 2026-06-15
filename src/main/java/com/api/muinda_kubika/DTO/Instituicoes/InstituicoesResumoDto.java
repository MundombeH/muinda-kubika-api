package com.api.muinda_kubika.DTO.Instituicoes;

import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class InstituicoesResumoDto {
    private UUID id;
    private Boolean isActive;
    private  String descricao;
    private Set<TipoInstituicaoEnum> tipoInstituicao = new HashSet<>();
    private String numeroDeTelefone;
    private String email;

}

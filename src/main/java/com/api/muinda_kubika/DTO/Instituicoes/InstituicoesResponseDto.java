package com.api.muinda_kubika.DTO.Instituicoes;

import com.api.muinda_kubika.DTO.Localizacao.BairroResumoDto;
import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class InstituicoesResponseDto extends DefaultDto {
    private  String descricao;
    private  Integer anoDeFundacao;
    private Set<TipoInstituicaoEnum> tipoInstituicao = new HashSet<>();
    private String numeroDeTelefone;
    private String email;
    private LocalTime horaioDeFuncionamento;
    private BairroResumoDto bairro;
    private Double latitude;
    private Double longitude;
}

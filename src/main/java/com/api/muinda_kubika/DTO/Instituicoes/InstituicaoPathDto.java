package com.api.muinda_kubika.DTO.Instituicoes;

import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class InstituicaoPathDto {
    private  String descricao;
    private  Integer anoDeFundacao;
    private Set<TipoInstituicaoEnum> tipoInstituicao = new HashSet<>();
    private String numeroDeTelefone;
    private String email;
    private LocalTime horaioDeFuncionamento;
    private UUID bairro;
    private Double latitude;
    private Double longitude;
}

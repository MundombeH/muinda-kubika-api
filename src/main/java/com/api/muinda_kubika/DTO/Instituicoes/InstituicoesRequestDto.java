package com.api.muinda_kubika.DTO.Instituicoes;

import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class InstituicoesRequestDto {

    @NotBlank(message = "Descrição da instituição é obrigatória")
    private  String descricao;

    @Positive(message = "Ano de fundação não pode ser negativo")
    @NotNull(message = "Ano de fundação não pode ser nulo")
    private  Integer anoDeFundacao;

    @NotNull(message = "O  tipo de estabelecimento é obrigatório")
    private Set<TipoInstituicaoEnum> tipoInstituicao = new HashSet<>();

    @NotBlank(message = "número de telefone da instituição é obrigatória")
    private String numeroDeTelefone;

    @Email
    @NotBlank(message = "Email da instituição é obrigatório")
    private String email;

    private LocalTime horaioDeFuncionamento;

    @NotNull(message = "O Bairro é obrigatorio")
    private UUID bairro;

    private Double latitude;
    private Double longitude;
}

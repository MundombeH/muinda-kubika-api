package com.api.muinda_kubika.model.Instituicao;

import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "INSTITUICOES")
@Entity
@Setter
@Getter
public class InstituicaoModel extends DefaultModel {

    @Column(nullable = false)
    private  String descricao;

    @Column(nullable = false)
    private  Integer anoDeFundacao;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "instituicao_tipo",
            joinColumns = @JoinColumn(name = "instituicao_id")
    )
    @Column(name = "tipo")
    private Set<TipoInstituicaoEnum> tipoInstituicao = new HashSet<>();

    @Column(nullable = false,unique = true)
    private String numeroDeTelefone;

    @Column(nullable = false,unique = true)
    private String email;

    private LocalTime horaioDeFuncionamento;

    @ManyToOne
    @JoinColumn(name = "bairro_id")
    private BairroModel bairro;

    @Min(-90)
    @Max(90)
    private Double latitude;

    @Min(-180)
    @Max(180)
    private Double longitude;

}

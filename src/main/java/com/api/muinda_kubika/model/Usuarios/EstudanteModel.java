package com.api.muinda_kubika.model.Usuarios;

import com.api.muinda_kubika.Enums.GeneroEnum;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USUARIOS_ESTUDANTES")
@Setter
@Getter
public class EstudanteModel extends DefaultModel {


    @Column(nullable = false)
    private String curso;
    @Column(nullable = false)
    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GeneroEnum genero;

    @Column(unique = true)
    private String identificacao;

    @ManyToOne
    @JoinColumn(name = "bairro_id")
    private BairroModel bairro;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private DefaultUserModel usuario;

    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private InstituicaoModel instituicao;

}

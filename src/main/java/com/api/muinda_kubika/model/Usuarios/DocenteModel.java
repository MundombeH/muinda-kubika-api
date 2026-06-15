package com.api.muinda_kubika.model.Usuarios;

import com.api.muinda_kubika.Enums.GeneroEnum;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import  java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "USUARIOS_DOCENTES")
public class DocenteModel extends DefaultModel {

    private String departamento;

    @Column(nullable = false,unique = true)
    private  String identificacao;

    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    @ManyToOne
    @JoinColumn(name = "bairro_id")
    private BairroModel bairro;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private DefaultUserModel usuario;

    @ManyToMany
    @JoinTable(name = "instituicoes_docentes",
    joinColumns = @JoinColumn( name = "docente_id"),
                inverseJoinColumns = @JoinColumn( name = "instituicao_id"))
    private Set<InstituicaoModel> instituicao = new HashSet<>();

}

package com.api.muinda_kubika.model.Usuarios;

import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name  = "USUARIO_ADMIN_INSTITUICAO")
@Setter
@Getter
public class AdminInstituicaoModel extends DefaultModel {

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private DefaultUserModel usuario;


    @ManyToMany
    @JoinTable(name = "admin_instituicao",
            joinColumns = @JoinColumn( name = "admin_instituicao_id"),
            inverseJoinColumns = @JoinColumn( name = "instituicao_id"))
    private Set<InstituicaoModel> instituicao = new HashSet<>();
}

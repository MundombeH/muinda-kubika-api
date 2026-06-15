package com.api.muinda_kubika.model.Roles_permissions;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "PERMISSIONS")
public class PermissionsModel extends DefaultModel {

    @Column(nullable = false, unique = true)
    private String descricao;


    @ManyToMany(mappedBy = "permissions")
    private Set<RolesModel> roles = new HashSet<>();
}

package com.api.muinda_kubika.model.Roles_permissions;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLES")
@Setter
@Getter
public class RolesModel extends DefaultModel {

    @Column(nullable = false)
    private String descricao;


    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionsModel> permissions = new HashSet<>();


}

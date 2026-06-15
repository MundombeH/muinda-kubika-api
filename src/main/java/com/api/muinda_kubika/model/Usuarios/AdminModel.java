package com.api.muinda_kubika.model.Usuarios;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "USUARIO_ADMIN")
public class AdminModel extends DefaultModel {

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private DefaultUserModel usuario;
}

package com.api.muinda_kubika.model.Categorias_Tags;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "CATEGORIAS")
@Entity
public class CategoriasModel extends DefaultModel {

    @Column(nullable = false)
    private  String descricao;


}

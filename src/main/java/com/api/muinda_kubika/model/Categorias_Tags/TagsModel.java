package com.api.muinda_kubika.model.Categorias_Tags;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "TAGS")
public class TagsModel extends DefaultModel {

    private String descricao;
}

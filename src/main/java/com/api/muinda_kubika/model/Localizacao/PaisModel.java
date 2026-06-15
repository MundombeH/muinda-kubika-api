package com.api.muinda_kubika.model.Localizacao;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PAIS")
@Setter
@Getter
public class PaisModel extends DefaultModel {

    private String descricao;

    @OneToMany(mappedBy = "pais")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Evita recursão

    @JsonBackReference
    private Set<ProvinciasModel> provincias = new HashSet<>();

}

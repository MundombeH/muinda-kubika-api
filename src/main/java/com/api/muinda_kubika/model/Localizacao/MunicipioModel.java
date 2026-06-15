package com.api.muinda_kubika.model.Localizacao;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MUNICIPIOS")
@Setter
@Getter
public class MunicipioModel extends DefaultModel {

    private String descricao;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    @JsonBackReference // O Filho volta para a província
    private ProvinciasModel provincia;

    @OneToMany(mappedBy = "municipio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<BairroModel> bairros = new HashSet<>();
}

package com.api.muinda_kubika.model.Localizacao;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "BAIRROS")
@Setter
@Getter
public class BairroModel extends DefaultModel {

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "municipio_id", nullable = false)
    @JsonBackReference
    private MunicipioModel municipio;

    @OneToMany(mappedBy = "bairro")
    private Set<InstituicaoModel> instituicao;
}

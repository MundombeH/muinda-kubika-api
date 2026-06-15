package com.api.muinda_kubika.model.Files;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "REPOSITORIO")
public class RepositorioModel extends DefaultModel {

    @OneToOne
    @JoinColumn(name = "documento_id", nullable = false,unique = true)
    private DocumentosModel documento;

    @URL
    private String urlGithub;

    @ElementCollection
    @CollectionTable(
            name = "repositorio_tecnologias_usadas",
            joinColumns = @JoinColumn(name = "repositorio_id")
    )
    @Column(name = "tecnologia")
    private Set<String> tecnologiasUsadas;

}

package com.api.muinda_kubika.model.Files;

import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Categorias_Tags.CategoriasModel;
import com.api.muinda_kubika.model.Categorias_Tags.TagsModel;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "DOCUMENTOS")
public class DocumentosModel extends DefaultModel {

    @Column(nullable = false)
    private String titulo;

    private String resumo;

    @ElementCollection
    @CollectionTable(
            name = "documento_autores",
            joinColumns = @JoinColumn(name = "documento_id")
    )
    @Column(name = "autor")
    private Set<String> autores = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private DefaultUserModel usuario;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private InstituicaoModel instituicao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumentoEnum tipoDeDocumento;

    @Enumerated(EnumType.STRING)
    private StatusDocumentoEnum status;

    @ManyToOne
    @JoinColumn(name = "aprovado_por")
    private DefaultUserModel aprovadoPor;

    private LocalDateTime dataAprovacao;

    @Column(name = "versao")
    private Integer versao;

    @Column(name = "capa_url")
    private String capaUrl;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FicheiroModel> ficheiros = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "documentos_categorias",
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private Set<CategoriasModel> categorias = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "documentos_tags",
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagsModel> tags = new HashSet<>();

}

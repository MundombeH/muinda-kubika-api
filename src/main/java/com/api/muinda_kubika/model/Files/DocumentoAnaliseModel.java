package com.api.muinda_kubika.model.Files;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "documento_analize")
@Setter
@Getter
public class DocumentoAnaliseModel extends DefaultModel {

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private DocumentosModel documento;

    @Column(name = "resumo_gerado_ia")
    private String resumoGeradoIA;

    @Column(name = "titulo_sugerido")
    private String tituloSugerido;

    @Column(name = "titulo_confianca")
    private Integer tituloConfianca;

    @Column(name = "categoria_sugerida")
    private String categoriaSugerida;

    @Column(name = "categoria_confianca")
    private Integer categoriaConfianca;

    @Column(name = "subcategoria_sugerida")
    private String subcategoriaSugerida;

    @Column(name = "subcategoria_confianca")
    private Integer subcategoriaConfianca;

    @ElementCollection
    @CollectionTable(
        name = "documento_analise_palavras_chave_ia",
        joinColumns = @JoinColumn(name = "documento_analise_id")
    )
    @AttributeOverrides({
        @AttributeOverride(name = "valor", column = @Column(name = "palavra")),
        @AttributeOverride(
            name = "confianca",
            column = @Column(name = "confianca")
        ),
    })
    private Set<SugestaoConfiancaModel> palavrasChaveIA = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "documento_analise_tags_sugeridas",
        joinColumns = @JoinColumn(name = "documento_analise_id")
    )
    @AttributeOverrides({
        @AttributeOverride(name = "valor", column = @Column(name = "tag")),
        @AttributeOverride(
            name = "confianca",
            column = @Column(name = "confianca")
        ),
    })
    private Set<SugestaoConfiancaModel> tagsSugeridas = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "documento_analise_tecnologias_sugeridas",
        joinColumns = @JoinColumn(name = "documento_analise_id")
    )
    @AttributeOverrides({
        @AttributeOverride(
            name = "valor",
            column = @Column(name = "tecnologia")
        ),
        @AttributeOverride(
            name = "confianca",
            column = @Column(name = "confianca")
        ),
    })
    private Set<SugestaoConfiancaModel> tecnologiasSugeridas = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "documento_analise_frameworks_sugeridos",
        joinColumns = @JoinColumn(name = "documento_analise_id")
    )
    @AttributeOverrides({
        @AttributeOverride(
            name = "valor",
            column = @Column(name = "framework")
        ),
        @AttributeOverride(
            name = "confianca",
            column = @Column(name = "confianca")
        ),
    })
    private Set<SugestaoConfiancaModel> frameworksSugeridos = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "documento_analise_conflitos",
        joinColumns = @JoinColumn(name = "documento_analise_id")
    )
    @Column(name = "conflito")
    private Set<String> conflitosDetectados = new HashSet<>();

    @Column(name = "motivo_rejeicao")
    private String motivoRejeicao;

    @Column(name = "observacao_admin")
    private String observacaoAdmin;

    private LocalDateTime dataProcessamento;

    private Integer versao;
}

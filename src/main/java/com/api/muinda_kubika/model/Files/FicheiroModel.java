package com.api.muinda_kubika.model.Files;

import com.api.muinda_kubika.Defaults.DefaultModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FICHEIRO")
@Getter
@Setter
public class FicheiroModel extends DefaultModel {
    private String nome;
    private String url;
    private Long tamanho;
    private String formato;
    private String mimeType;
    @Column(nullable = false, unique = true,name = "public_id")
    private String publicId;
    @Column(name = "resource_type")
    private String resourceType;
    private Integer versao;
    private String checksum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private DocumentosModel documento;


}
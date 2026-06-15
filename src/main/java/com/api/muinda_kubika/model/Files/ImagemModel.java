//package com.api.muinda_kubika.model.files;
//
//import com.api.muinda_kubika.Enums.TipoImagemEnum;
//import com.api.muinda_kubika.defaults.DefaultModel;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
//@Entity
//@Table(name = "IMAGEM")
//public class ImagemModel extends DefaultModel {
//    private String nome;
//    private String url;
//
//    private Boolean capa = false;
//    private Integer ordem;
//    private String mimeType;
//    private String checksum;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "file_id")
//    private DocumentosModel documento;
//    private Long tamanho;
//}
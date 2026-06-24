package com.api.muinda_kubika.Enums;

public enum TipoDocumentoEnum {
    MONOGRAFIA("Monografia"),
    RELATORIO("Relatório"),
    ARTIGO("Artigo"),
    REPOSITORIO("Repósitorio"),
    ZIP("ZIP"),
    LIVRO("Livro"),
    SEMINARIO("Seminário"),
    INDEFINIDO("Indefinido");

    private  final String descricao;

    TipoDocumentoEnum(String descricao){
        this.descricao = descricao;
    }
    public String getTipoDocumentoEnum(){
        return descricao;
    }
}

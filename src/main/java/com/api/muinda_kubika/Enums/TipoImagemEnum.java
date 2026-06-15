package com.api.muinda_kubika.Enums;


public enum TipoImagemEnum {
    CAPA("capa"),
    screenshots("screenshots");

    public  final String tipo;

    TipoImagemEnum(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}

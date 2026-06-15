package com.api.muinda_kubika.Enums;

public enum TipoInstituicaoEnum {
    SUPERIOR("Superior"),
    ICICLO("I clico"),
    IICICLO("II ciclo"),
    MEDIO("Médio");

    private final String tipo;

    TipoInstituicaoEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}

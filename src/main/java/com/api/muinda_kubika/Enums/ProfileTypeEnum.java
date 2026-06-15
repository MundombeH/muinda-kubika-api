package com.api.muinda_kubika.Enums;

public enum ProfileTypeEnum {
    ADMIN("Administrador"),
    ADMIN_INSTITUICAO("Administrador de Instituição"),
    DOCENTE("Docente"),
    ESTUDANTE("Estudante");

    private final String tipo;

    ProfileTypeEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}

package com.api.muinda_kubika.Enums;

public enum GeneroEnum {
    MASCULINO("Masculino"),
    NAOBINARIO("Não Binário"),
    FEMINIO("Feminio");

    private final String genero;

    GeneroEnum(String genero) {
        this.genero = genero;
    }

    public String getGenero() {
        return genero;
    }
}

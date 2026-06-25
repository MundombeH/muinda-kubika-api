package com.api.muinda_kubika.DTO.Files.AnalizeIA;

import java.util.Set;

public class ConfirmarAnaliseRequestDto {

    private Set<String> tecnologiasSugeridas;
    private Set<String> frameworksSugeridos;
    private Set<String> palavrasChaveIA;
    private String subcategoriaSugerida;
    private Integer subcategoriaConfianca;

    public Set<String> getTecnologiasSugeridas() { return tecnologiasSugeridas; }
    public void setTecnologiasSugeridas(Set<String> tecnologiasSugeridas) { this.tecnologiasSugeridas = tecnologiasSugeridas; }

    public Set<String> getFrameworksSugeridos() { return frameworksSugeridos; }
    public void setFrameworksSugeridos(Set<String> frameworksSugeridos) { this.frameworksSugeridos = frameworksSugeridos; }

    public Set<String> getPalavrasChaveIA() { return palavrasChaveIA; }
    public void setPalavrasChaveIA(Set<String> palavrasChaveIA) { this.palavrasChaveIA = palavrasChaveIA; }

    public String getSubcategoriaSugerida() { return subcategoriaSugerida; }
    public void setSubcategoriaSugerida(String subcategoriaSugerida) { this.subcategoriaSugerida = subcategoriaSugerida; }

    public Integer getSubcategoriaConfianca() { return subcategoriaConfianca; }
    public void setSubcategoriaConfianca(Integer subcategoriaConfianca) { this.subcategoriaConfianca = subcategoriaConfianca; }
}

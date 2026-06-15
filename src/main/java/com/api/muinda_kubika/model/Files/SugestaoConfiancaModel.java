package com.api.muinda_kubika.model.Files;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class SugestaoConfiancaModel {
    private String valor;
    private Integer confianca;
}

package com.api.muinda_kubika.DTO.Tags;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagsRequestTo {
    @NotEmpty(message = "descricao é obrigatoria")
    private String descricao;
}

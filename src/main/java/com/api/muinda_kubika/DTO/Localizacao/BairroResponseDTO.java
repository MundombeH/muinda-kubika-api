package com.api.muinda_kubika.DTO.Localizacao;


import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BairroResponseDTO extends DefaultDto {


    private MunicipiosResumoDTO municipio;
    private String descricao;
}

package com.api.muinda_kubika.DTO.Localizacao;


import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MunicipiosResponseDTO extends DefaultDto {


    private String descricao;
    private ProvinciasResumoDTO provinciaId;
}

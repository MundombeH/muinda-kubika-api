package com.api.muinda_kubika.DTO.Localizacao;


import java.util.UUID;

import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProvinciasResponseDTO extends DefaultDto {

    private UUID id;
    private String descricao;
    private PaisResumoDTO pais;

    // private Set<MunicipiosResumoDTO> municipios = new HashSet<>();
}

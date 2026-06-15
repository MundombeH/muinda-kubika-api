package com.api.muinda_kubika.DTO.Localizacao;

import java.util.UUID;


import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PaisesResponseDTO extends DefaultDto {


    private UUID id;
    private String descricao;

}

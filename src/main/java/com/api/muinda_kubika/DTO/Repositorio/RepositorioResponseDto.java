package com.api.muinda_kubika.DTO.Repositorio;

import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosResponseDto;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Setter
@Getter
public class RepositorioResponseDto extends  DefaultDto{

    private DocumentosResponseDto documento;
    private String urlGithub;

    private Set<String> tecnologiasUsadas;

}

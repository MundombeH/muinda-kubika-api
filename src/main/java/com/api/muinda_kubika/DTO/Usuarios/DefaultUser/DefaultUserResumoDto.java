package com.api.muinda_kubika.DTO.Usuarios.DefaultUser;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class DefaultUserResumoDto {
    private UUID id;
    private String nome;
    private String email;
    private String numeroDeTelefone;

}

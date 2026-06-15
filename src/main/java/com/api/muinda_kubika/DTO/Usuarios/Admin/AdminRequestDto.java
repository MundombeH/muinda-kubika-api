package com.api.muinda_kubika.DTO.Usuarios.Admin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AdminRequestDto {
    @NotNull(message = "O Usuário é obrigatório")
    private UUID usuario;
}

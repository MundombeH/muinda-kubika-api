package com.api.muinda_kubika.DTO.Usuarios.AdminInstituicao;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class AdminInstituicaoRequestDto {
    @NotNull(message = "O Usuário é obrigatória")
    private UUID usuario;

    @Size(min = 1, message = "Tem de selecionar pelo menos uma instituição")
    private Set<UUID> instituicoes = new HashSet<>();
}

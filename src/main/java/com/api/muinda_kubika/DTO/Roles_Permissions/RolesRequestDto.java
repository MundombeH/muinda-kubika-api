package com.api.muinda_kubika.DTO.Roles_Permissions;


import com.api.muinda_kubika.Defaults.DefaultDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class RolesRequestDto extends DefaultDto {

    @NotBlank(message = "A descricao da role não pode estar em branco")
    @NotNull(message = "A descricao da role não pode ser nulo")
    @Size(min = 3, message = "A descricao da role deve ter no mínimo 3 caracteres")
    private  String descricao;

    @NotNull(message = "As páginas não podem ser nulas")
    @Size(min = 1, message = "A role deve ter pelo menos uma permissao")
    private Set<UUID> permissions = new HashSet<>();
}

package com.api.muinda_kubika.DTO.Roles_Permissions;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class RolesResumoDto {
    private  String descricao;
    private UUID id;
    private Set<PermissionResumoDto> permissions;
    private Boolean isActive;
}

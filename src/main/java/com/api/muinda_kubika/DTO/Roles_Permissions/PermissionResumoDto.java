package com.api.muinda_kubika.DTO.Roles_Permissions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class PermissionResumoDto {
    private UUID id;
    private String descricao;
    private Boolean isActive;

}

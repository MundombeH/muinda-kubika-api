package com.api.muinda_kubika.DTO.Roles_Permissions;


import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class RolesResponseDto  extends DefaultDto {
    private UUID id;
    private  String descricao;
    private Set<PermissionsResponseDto> permissions = new HashSet<>();
}

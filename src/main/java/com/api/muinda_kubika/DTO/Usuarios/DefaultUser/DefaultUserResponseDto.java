package com.api.muinda_kubika.DTO.Usuarios.DefaultUser;

import com.api.muinda_kubika.DTO.Roles_Permissions.RolesResumoDto;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class DefaultUserResponseDto extends DefaultDto {

    private String nome;

    private String email;

    private String numeroDeTelefone;

    private LocalDate dataDeNascimento;

    private Set<RolesResumoDto> roles = new HashSet<>();



}

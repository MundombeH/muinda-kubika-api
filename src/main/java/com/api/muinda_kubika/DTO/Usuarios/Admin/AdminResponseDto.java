package com.api.muinda_kubika.DTO.Usuarios.Admin;

import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Defaults.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminResponseDto extends DefaultDto {
    private DefaultUserResumoDto usuario;

}

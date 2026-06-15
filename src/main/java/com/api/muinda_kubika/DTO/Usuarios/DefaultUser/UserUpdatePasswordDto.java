package com.api.muinda_kubika.DTO.Usuarios.DefaultUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePasswordDto {
    @NotBlank(message = "A password atual é obrigatória")
    @Size(min = 6, message = "A password deve ter no mínimo 6 caracteres")
    private String currentPassword;
    @NotBlank(message = "A nova password é obrigatória")
    @Size(min = 6, message = "A password deve ter no mínimo 6 caracteres")
    
    private String newPassword;
    @NotBlank(message = "A confirmação da password é obrigatória")
    @Size(min = 6, message = "A password deve ter no mínimo 6 caracteres")
    private String confirmPassword;
}

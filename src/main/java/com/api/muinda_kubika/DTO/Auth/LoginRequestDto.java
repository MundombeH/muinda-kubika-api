package com.api.muinda_kubika.DTO.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Email ou telefone é obrigatório")
    private String identificador;

    @NotBlank(message = "Password é obrigatória")
    private String password;
}

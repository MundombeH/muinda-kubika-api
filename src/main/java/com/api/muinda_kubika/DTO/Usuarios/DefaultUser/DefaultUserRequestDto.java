package com.api.muinda_kubika.DTO.Usuarios.DefaultUser;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DefaultUserRequestDto {

    @NotBlank(message = "Password é obrigatória")
    @Size(min = 6, message = "A password tem de ter pelo menos 6 caracteres")
    private String password;

    @NotBlank(message = "O Nome é obrigatório")
    private String nome;

    @Email(message = "Formato de e-mail errado")
    @NotBlank(message = "O E-mail é obrigatória")
    private String email;

    @NotBlank(message = "O número de telefone é obrigatória ")
    private String numeroDeTelefone;

    @Past(message = "A data de nascimento têm de estar no passado")
    private LocalDate dataDeNascimento;
}

package com.api.muinda_kubika.DTO.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {

    @NotBlank(message = "Refresh token é obrigatório")
    private String refreshToken;
}

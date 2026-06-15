package com.api.muinda_kubika.DTO.Auth;

import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String token;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresInMs;
    private Long refreshExpiresInMs;
    private UUID userId;
    private Set<String> roles;
    private Set<String> permissions;
}

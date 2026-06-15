package com.api.muinda_kubika.Controller.Auth;

import com.api.muinda_kubika.DTO.Auth.LoginRequestDto;
import com.api.muinda_kubika.DTO.Auth.LoginResponseDto;
import com.api.muinda_kubika.DTO.Auth.RefreshTokenRequestDto;
import com.api.muinda_kubika.Exceptions.InvalidCredentialsException;
import com.api.muinda_kubika.Service.Security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
        @Valid @RequestBody LoginRequestDto dto
    ) throws InvalidCredentialsException {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
        @Valid @RequestBody RefreshTokenRequestDto dto
    ) {
        return ResponseEntity.ok(authService.refresh(dto.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @Valid @RequestBody RefreshTokenRequestDto dto
    ) {
        authService.logout(dto.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}

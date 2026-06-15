package com.api.muinda_kubika.Controller.User;

import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserRequestDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.UserUpdatePasswordDto;
import com.api.muinda_kubika.Service.Usuarios.DefaultUserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class DefaultUserController {

    private final DefaultUserService userService;

    public DefaultUserController(DefaultUserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @GetMapping("")
    public ResponseEntity<List<DefaultUserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @GetMapping("/me")
    public ResponseEntity<DefaultUserResponseDto> getAll(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(userService.getOneUser(userId));
    }

    @PostMapping("")
    public ResponseEntity<DefaultUserResponseDto> post(
        @Valid @RequestBody DefaultUserRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            userService.createUser(dto)
        );
    }

    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @PutMapping("")
    public ResponseEntity<DefaultUserResponseDto> put(
        @Valid @RequestBody DefaultUserRequestDto dto,
        Authentication auth
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @PatchMapping("/senha")
    public ResponseEntity<String> updatePassword(
        @Valid @RequestBody UserUpdatePasswordDto dto,
        Authentication auth
    ) {
        UUID userId = UUID.fromString(auth.getName());
        System.out.println("AUTH NAME: " + auth.getName());
        return ResponseEntity.ok(userService.updatePassword(userId, dto));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

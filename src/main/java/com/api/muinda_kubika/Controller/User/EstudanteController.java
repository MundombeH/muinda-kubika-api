package com.api.muinda_kubika.Controller.User;

import com.api.muinda_kubika.DTO.Usuarios.Estudante.EstudanteCriarDto;
import com.api.muinda_kubika.DTO.Usuarios.Estudante.EstudanteRequestDto;
import com.api.muinda_kubika.DTO.Usuarios.Estudante.EstudanteResponseDto;
import com.api.muinda_kubika.Service.Usuarios.EstudanteService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario/estudante")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(EstudanteService estudanteService) {
        this.estudanteService = estudanteService;
    }

    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @GetMapping("")
    public ResponseEntity<List<EstudanteResponseDto>> getAll() {
        return ResponseEntity.ok(estudanteService.getAllEstudantes());
    }

    @PreAuthorize("@roleChecker.hasAnyActiveRole(authentication, 'ROLE_USUARIO', 'ROLE_ESTUDANTE')")
    @GetMapping("/me")
    public ResponseEntity<EstudanteResponseDto> getMe(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(estudanteService.getOne(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<String> post(
        Authentication auth,
        @Valid @RequestBody EstudanteCriarDto dto
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            estudanteService.criarPerfilEstudante(userId, dto)
        );
    }

    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @PatchMapping("")
    public ResponseEntity<EstudanteResponseDto> put(
        @Valid @RequestBody EstudanteRequestDto dto,
        Authentication auth
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(estudanteService.UpdateEstudante(dto, userId));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'PERFIL_APROVAR')"
    )
    @PatchMapping("/{estudanteId}/activar")
    public ResponseEntity<String> activar(
        @PathVariable UUID estudanteId,
        Authentication auth
    ) {
        UUID adminId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(
            estudanteService.activarPerfilEstudante(adminId, estudanteId)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        estudanteService.deleteEstudante(id);
        return ResponseEntity.noContent().build();
    }
}

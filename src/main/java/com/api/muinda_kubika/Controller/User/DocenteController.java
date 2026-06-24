package com.api.muinda_kubika.Controller.User;

import com.api.muinda_kubika.DTO.Usuarios.Docente.DocenteCriarDto;
import com.api.muinda_kubika.DTO.Usuarios.Docente.DocenteRequestDto;
import com.api.muinda_kubika.DTO.Usuarios.Docente.DocenteResponseDto;
import com.api.muinda_kubika.Service.Usuarios.DocenteService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario/docente")
public class DocenteController {

    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    // LISTAR TODOS (admin ou admin instituição)
    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @GetMapping
    public ResponseEntity<List<DocenteResponseDto>> getAll() {
        return ResponseEntity.ok(docenteService.getAllDocente());
    }

    // MEU PERFIL
    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_USUARIO', 'ROLE_DOCENTE')"
    )
    @GetMapping("/me")
    public ResponseEntity<DocenteResponseDto> getMe(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(docenteService.getOne(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<String> create(
        Authentication auth,
        @Valid @RequestBody DocenteCriarDto dto
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            docenteService.criarPerfilDocente(userId, dto)
        );
    }

    // UPDATE PERFIL DOCENTE
    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_DOCENTE')"
    )
    @PatchMapping
    public ResponseEntity<DocenteResponseDto> update(
        Authentication auth,
        @RequestBody @Valid DocenteRequestDto dto
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(docenteService.updateDocente(dto, userId));
    }

    // ATIVAR PERFIL
    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAuthority(authentication, 'PERFIL_APROVAR')"
    )
    @PatchMapping("/{docenteId}/activar")
    public ResponseEntity<String> activate(
        @PathVariable UUID docenteId,
        Authentication auth
    ) {
        UUID adminId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(
            docenteService.activarPerfilDocente(adminId, docenteId)
        );
    }

    // DELETE
    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        docenteService.deleteDocente(userId);
        return ResponseEntity.noContent().build();
    }
}

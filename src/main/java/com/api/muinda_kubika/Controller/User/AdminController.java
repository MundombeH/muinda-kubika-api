package com.api.muinda_kubika.Controller.User;

import com.api.muinda_kubika.DTO.Usuarios.Admin.AdminResponseDto;
import com.api.muinda_kubika.Service.Usuarios.AdminService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @GetMapping("")
    public ResponseEntity<List<AdminResponseDto>> getAll() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @GetMapping("/me")
    public ResponseEntity<AdminResponseDto> getOne(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(adminService.getOneAdmin(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<String> post(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            adminService.criarPerfilAdmin(userId)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAnyAuthority(authentication, 'PERFIL_APROVAR', 'USUARIO_GERIR')"
    )
    @PatchMapping("/{adminId}/activar")
    public ResponseEntity<String> put(
        @PathVariable UUID adminId,
        Authentication auth
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(
            adminService.activarPerfilAdmin(userId, adminId)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}

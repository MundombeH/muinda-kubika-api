package com.api.muinda_kubika.Controller.User;

import com.api.muinda_kubika.DTO.Usuarios.AdminInstituicao.AdminInstituicaoReponseDto;
import com.api.muinda_kubika.DTO.Usuarios.AdminInstituicao.AdminInstituicaoRequestDto;
import com.api.muinda_kubika.Service.Usuarios.AdminInstituicaoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario/admin/instituicao")
public class AdminInstituicaoController {

    private final AdminInstituicaoService adminInstituicaoService;

    public AdminInstituicaoController(
        AdminInstituicaoService adminInstituicaoService
    ) {
        this.adminInstituicaoService = adminInstituicaoService;
    }

    // LISTAR TODOS (só admins podem ver tudo)
    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @GetMapping
    public ResponseEntity<List<AdminInstituicaoReponseDto>> getAll() {
        return ResponseEntity.ok(
            adminInstituicaoService.getAllAdminsInstituicao()
        );
    }

    // VER O PRÓPRIO PERFIL
    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN_INSTITUICAO', 'ROLE_ADMIN')"
    )
    @GetMapping("/me")
    public ResponseEntity<AdminInstituicaoReponseDto> getMe(
        Authentication auth
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(
            adminInstituicaoService.getOneAdminInstituicao(userId)
        );
    }

    // CRIAR PERFIL (somente usuário base)
    @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @PostMapping
    public ResponseEntity<String> create(
        Authentication auth,
        @RequestBody @Valid AdminInstituicaoRequestDto dto
    ) {
        UUID userId = UUID.fromString(auth.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(
            adminInstituicaoService.criarPerfilAdminInstituicao(userId, dto)
        );
    }

    // ATIVAR PERFIL (admin ou admin instituição)
    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAnyAuthority(authentication, 'PERFIL_APROVAR', 'USUARIO_GERIR')"
    )
    @PatchMapping("/{adminId}/activar")
    public ResponseEntity<String> activate(
        @PathVariable UUID adminId,
        Authentication auth
    ) {
        UUID activadorId = UUID.fromString(auth.getName());

        return ResponseEntity.ok(
            adminInstituicaoService.activarPerfilAdmin(activadorId, adminId)
        );
    }

    // DESATIVAR PERFIL
    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> delete(@PathVariable UUID adminId) {
        adminInstituicaoService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }
}

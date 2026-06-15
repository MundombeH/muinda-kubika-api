package com.api.muinda_kubika.Controller.RolesPermissions;

import com.api.muinda_kubika.DTO.Roles_Permissions.RolesRequestDto;
import com.api.muinda_kubika.DTO.Roles_Permissions.RolesResponseDto;
import com.api.muinda_kubika.Service.Roles_Permissions.RolesServices;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("role")
public class RolesController {

    private final RolesServices rolesService;

    public RolesController(RolesServices rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping("")
    public ResponseEntity<List<RolesResponseDto>> getAll() {
        return ResponseEntity.ok(rolesService.getallActiveRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(rolesService.getOneRole(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PostMapping("")
    public ResponseEntity<Object> post(
        @Valid @RequestBody RolesRequestDto rolesRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            rolesService.creatRole(rolesRequestDto)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        try {
            rolesService.deleteRole(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                "Role deleted successfully"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Failed to delete role"
            );
        }
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PutMapping("{id}")
    public ResponseEntity<Object> put(
        @Valid @PathVariable UUID id,
        @RequestBody @Valid RolesRequestDto rolesRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            rolesService.updateRoles(id, rolesRequestDto)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PatchMapping("/activate/{id}")
    public RolesResponseDto activateRole(@PathVariable UUID id) {
        return rolesService.activateRole(id);
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PatchMapping("/deactivate/{id}")
    public RolesResponseDto deactivateRole(@PathVariable UUID id) {
        return rolesService.deactivateRole(id);
    }
}

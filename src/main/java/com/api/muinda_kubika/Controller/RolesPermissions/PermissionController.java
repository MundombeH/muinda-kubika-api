package com.api.muinda_kubika.Controller.RolesPermissions;

import com.api.muinda_kubika.DTO.Roles_Permissions.PermissionsRequestDto;
import com.api.muinda_kubika.DTO.Roles_Permissions.PermissionsResponseDto;
import com.api.muinda_kubika.Service.Roles_Permissions.PermissionsService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionsService permissionsService;

    public PermissionController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @GetMapping("")
    public ResponseEntity<List<PermissionsResponseDto>> getAll() {
        return ResponseEntity.status(200).body(
            permissionsService.getAllActivePermissions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(200).body(
                permissionsService.getOnePermission(id)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PostMapping("")
    public ResponseEntity<Object> post(
        @RequestBody @Valid PermissionsRequestDto per
    ) {
        try {
            return ResponseEntity.status(200).body(
                permissionsService.createPermission(per)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Object> put(
        @PathVariable UUID id,
        @RequestBody @Valid PermissionsRequestDto permissionsRequestDto
    ) {
        try {
            return ResponseEntity.status(200).body(
                permissionsService.updatePermission(id, permissionsRequestDto)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        try {
            permissionsService.deletePermission(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                e.getMessage()
            );
        }
    }
}

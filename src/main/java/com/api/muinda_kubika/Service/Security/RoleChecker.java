package com.api.muinda_kubika.Service.Security;

import com.api.muinda_kubika.Repository.Roles_Permissions.PermissionsRepository;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("roleChecker")
public class RoleChecker {

    private final RolesRepository rolesRepository;
    private final PermissionsRepository permissionsRepository;

    public RoleChecker(RolesRepository rolesRepository, PermissionsRepository permissionsRepository) {
        this.rolesRepository = rolesRepository;
        this.permissionsRepository = permissionsRepository;
    }

    public boolean hasActiveRole(Authentication authentication, String roleName) {
        if (!isAuthenticated(authentication) || roleName == null || roleName.isBlank()) {
            System.out.println("❌ RoleChecker: Usuário não está autenticado.");
            return false;
        }
    
        // Printa o que veio de dentro do seu Token para você ver
        System.out.println("👉 Authorities vindas do Token: " + authentication.getAuthorities());
        System.out.println("👉 Role que a rota está pedindo: " + roleName);
    
        boolean temNoToken = hasAuthorityInToken(authentication, roleName);
        boolean ativaNoBanco = rolesRepository.existsByDescricaoAndIsActiveTrue(roleName);

        return temNoToken && ativaNoBanco;
    }

    public boolean hasAnyActiveRole(Authentication authentication, String... roleNames) {
        if (!isAuthenticated(authentication) || roleNames == null || roleNames.length == 0) {
            return false;
        }

        return Arrays.stream(roleNames)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(role -> !role.isBlank())
                .anyMatch(role -> hasActiveRole(authentication, role));
    }

    public boolean hasAuthority(Authentication authentication, String permissionName) {
        if (!isAuthenticated(authentication) || permissionName == null || permissionName.isBlank()) {
            return false;
        }

        return hasAuthorityInToken(authentication, permissionName)
                && permissionsRepository.existsByDescricaoAndIsActiveTrue(permissionName);
    }

    public boolean hasAnyAuthority(Authentication authentication, String... permissionNames) {
        if (!isAuthenticated(authentication) || permissionNames == null || permissionNames.length == 0) {
            return false;
        }

        return Arrays.stream(permissionNames)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(permission -> !permission.isBlank())
                .anyMatch(permission -> hasAuthority(authentication, permission));
    }

    private boolean hasAuthorityInToken(Authentication authentication, String authorityName) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authorityName::equals);
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}

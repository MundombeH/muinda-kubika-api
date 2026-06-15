package com.api.muinda_kubika.Service.Roles_Permissions;

import com.api.muinda_kubika.DTO.Roles_Permissions.RolesRequestDto;
import com.api.muinda_kubika.DTO.Roles_Permissions.RolesResponseDto;
import com.api.muinda_kubika.Exceptions.RoleNameAlreayExistException;
import com.api.muinda_kubika.Exceptions.RoleNotFoundException;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.model.Roles_permissions.PermissionsModel;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RolesServices {
    private  final RolesRepository rolesRepository;
    private  final PermissionsService permissionsService;

    public RolesServices(RolesRepository rolesRepository, PermissionsService permissionsService) {
        this.rolesRepository = rolesRepository;
        this.permissionsService = permissionsService;
    }

    public List<RolesResponseDto> getallActiveRoles(){
        return rolesRepository.findByIsActiveTrue()
                .stream()
        .map(this::mapToService).collect(Collectors.toList());
    }

    public RolesResponseDto creatRole(RolesRequestDto rolesRequestDto){

    RolesModel role = new  RolesModel();

        if (rolesRequestDto.getDescricao() != null &&
                rolesRepository.existsByDescricao(rolesRequestDto.getDescricao())) {
            throw new RoleNameAlreayExistException(rolesRequestDto.getDescricao());
        }
        BeanUtils.copyProperties(rolesRequestDto, role);
        role.setIsActive(true);

        if (rolesRequestDto.getPermissions() != null && !rolesRequestDto.getPermissions().isEmpty()) {

            Set<PermissionsModel> permissions =
                    permissionsService.getActivePermissionsByIds(rolesRequestDto.getPermissions());

            role.setPermissions(permissions);
// Atualiza o lado inverso
            for (PermissionsModel p : permissions) {
                p.getRoles().add(role);
            }
        }

        RolesModel savedRole = rolesRepository.save(role);
        return  mapToService(savedRole);

    }
    @Transactional
    public RolesResponseDto updateRoles(UUID id, RolesRequestDto rolesRequestDto) {
        RolesModel role = rolesRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(RoleNotFoundException:: new);

        if (rolesRequestDto.getDescricao() != null) {
            if (rolesRepository.existsByDescricao(rolesRequestDto.getDescricao())) {
                throw new RoleNameAlreayExistException(rolesRequestDto.getDescricao());
            }
        }

        role.setDescricao(rolesRequestDto.getDescricao());

        if (rolesRequestDto.getPermissions() != null) {
            Set<PermissionsModel> permissions = permissionsService.getActivePermissionsByIds(rolesRequestDto.getPermissions());
            if (permissions.size() != rolesRequestDto.getPermissions().size()) {
                throw new RuntimeException("Uma ou mais páginas não foram encontradas ou estão inativas");
            }

            // Limpa as páginas antigas e adiciona as novas
            // Primeiro, remove a role das páginas antigas para manter a consistência
            // bidirecional
            for (PermissionsModel oldPagina : role.getPermissions()) {
                oldPagina.getRoles().remove(role);
            }
            role.getPermissions().clear();
            role.getPermissions().addAll(permissions);

            // Sincronização bidirecional: adiciona a role às novas páginas
            for (PermissionsModel p : permissions) {
                p.getRoles().add(role);
            }
        }

        RolesModel updatedRole = rolesRepository.save(role);

        return mapToService(updatedRole);
    }

    public void deleteRole(UUID id) {
        RolesModel role = rolesRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(RoleNotFoundException::new);

        role.setIsActive(false);
        rolesRepository.save(role);

    }

    // Buscar role por ID (apenas ativa)
    public RolesResponseDto getOneRole(UUID id) {
        RolesModel role = rolesRepository.findByIdAndIsActiveTrue(id).orElseThrow(RoleNotFoundException::new);
        return mapToService(role);
    }

    // Activar e desativar role
    public RolesResponseDto activateRole(UUID id) {
        RolesModel role = rolesRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
        role.setIsActive(true);
        return mapToService(rolesRepository.save(role));
    }

    public RolesResponseDto deactivateRole(UUID id) {
        RolesModel role = rolesRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
        role.setIsActive(false);
        return mapToService(rolesRepository.save(role));
    }


    public RolesResponseDto mapToService(RolesModel role) {
        RolesResponseDto dto = new RolesResponseDto();
        dto.setId(role.getId());
        dto.setDescricao(role.getDescricao());
        dto.setIsActive(role.getIsActive());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());

        if (role.getPermissions() != null) {
            dto.setPermissions(role.getPermissions().stream()
                    .map(permissionsService::mapToService)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}

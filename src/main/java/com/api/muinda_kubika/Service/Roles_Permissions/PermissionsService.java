package com.api.muinda_kubika.Service.Roles_Permissions;

import com.api.muinda_kubika.DTO.Roles_Permissions.PermissionsRequestDto;
import com.api.muinda_kubika.DTO.Roles_Permissions.PermissionsResponseDto;
import com.api.muinda_kubika.Repository.Roles_Permissions.PermissionsRepository;
import com.api.muinda_kubika.model.Roles_permissions.PermissionsModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermissionsService {
    private  final PermissionsRepository permissionsRepository;

    public PermissionsService(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    public List<PermissionsResponseDto> getAllActivePermissions(){
        return permissionsRepository.findByIsActiveTrue().stream().filter(PermissionsModel::getIsActive).map(this::mapToService).collect(Collectors.toList());
    }
    public PermissionsResponseDto getOnePermission(UUID id) {
        PermissionsModel permissionsModel = permissionsRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("permissão nao encontrada ou inativa"));
        return mapToService(permissionsModel);
    }

    public PermissionsResponseDto createPermission(PermissionsRequestDto permissionsRequestDto){

        if (permissionsRepository.existsByDescricao(permissionsRequestDto.getDescricao())) {
            throw new RuntimeException("Permissão já existe!");
        }
        PermissionsModel permissionsModel = new PermissionsModel();
        permissionsModel.setDescricao(permissionsRequestDto.getDescricao());

        PermissionsModel saved = permissionsRepository.save(permissionsModel);
        return  mapToService(saved);

    }

    @Transactional
    public PermissionsResponseDto updatePermission(UUID id,PermissionsRequestDto permissionsRequestDto){
        PermissionsModel permissionsModel =permissionsRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new EntityNotFoundException("Permission não encontrado!"));
        permissionsModel.setDescricao(permissionsRequestDto.getDescricao());

        PermissionsModel saved = permissionsRepository.save(permissionsModel);
        return  mapToService(saved);

    }

    @Transactional
    public void deletePermission(UUID id){
        PermissionsModel permissionsModel = permissionsRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new EntityNotFoundException("Permission não encontrado!"));

        permissionsModel.setIsActive(false);
        permissionsModel.setUpdatedAt(LocalDateTime.now());
        permissionsRepository.save(permissionsModel);

    }

    public Set<PermissionsModel> getActivePermissionsByIds(Set<UUID> ids) {
        return permissionsRepository.findAllByIdInAndIsActiveTrue(ids);
    }

    public PermissionsResponseDto mapToService(PermissionsModel permissionsModel){
        PermissionsResponseDto  permissionsResponseDto= new PermissionsResponseDto();

        permissionsResponseDto.setId(permissionsModel.getId());
        permissionsResponseDto.setDescricao(permissionsModel.getDescricao());
        permissionsResponseDto.setIsActive(permissionsModel.getIsActive());
        permissionsResponseDto.setCreatedAt(permissionsModel.getCreatedAt());
        permissionsResponseDto.setUpdatedAt(permissionsModel.getUpdatedAt());

        return  permissionsResponseDto;
    }



}

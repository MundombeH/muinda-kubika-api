package com.api.muinda_kubika.Service.Usuarios;

import com.api.muinda_kubika.DTO.Usuarios.Admin.AdminResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import com.api.muinda_kubika.Exceptions.ProfileAlreadyExistsException;
import com.api.muinda_kubika.Exceptions.RoleNotFoundException;
import com.api.muinda_kubika.Exceptions.UserNotFoundException;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.Repository.Usuarios.AdminRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Usuarios.AdminModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final DefaultUserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final ProfileApprovalService profileApprovalService;

    public AdminService(AdminRepository adminRepository, DefaultUserRepository userRepository,
            RolesRepository rolesRepository, ProfileApprovalService profileApprovalService) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.profileApprovalService = profileApprovalService;
    }

    public List<AdminResponseDto> getAllAdmins() {
        return adminRepository.findByIsActiveTrue().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public AdminResponseDto getOneAdmin(UUID userId) {
        AdminModel admin = adminRepository.findByUsuarioIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return mapToDto(admin);
    }

    @Transactional
    public String criarPerfilAdmin(UUID userId) {
        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (adminRepository.existsByUsuarioId(userId)) {
            throw new ProfileAlreadyExistsException(userId);

        }
        AdminModel adminModel = new AdminModel();
        adminModel.setIsActive(false);
        adminModel.setUsuario(user);
        AdminModel saved = adminRepository.save(adminModel);
        profileApprovalService.createPendingApproval(ProfileTypeEnum.ADMIN, saved.getId(), userId);
        return "Perfil de administrador criado com susesso!";

    }

    @Transactional
    public String activarPerfilAdmin(UUID userId, UUID adminId) {
        return profileApprovalService.approveByProfile(userId, ProfileTypeEnum.ADMIN, adminId);
    }

    @Transactional
    public void deleteAdmin(UUID userId) {
        AdminModel admin = adminRepository.findByUsuarioIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        RolesModel roleAdmin = rolesRepository
                .findByDescricao("ROLE_ADMIN")
                .orElseThrow(RoleNotFoundException::new);

        admin.getUsuario()
                .getRoles()
                .remove(roleAdmin);

        userRepository.save(admin.getUsuario());
        admin.setIsActive(false);
        adminRepository.save(admin);

    }

    private AdminResponseDto mapToDto(AdminModel adminModel) {
        AdminResponseDto dto = new AdminResponseDto();
        dto.setId(adminModel.getId());
        dto.setIsActive(adminModel.getIsActive());
        dto.setCreatedAt(adminModel.getCreatedAt());
        dto.setUpdatedAt(adminModel.getUpdatedAt());

        if (adminModel.getUsuario() != null) {
            dto.setUsuario(mapToUser(adminModel.getUsuario()));

        }
        return dto;
    }

    private DefaultUserResumoDto mapToUser(@NonNull DefaultUserModel usuario) {
        DefaultUserResumoDto dto = new DefaultUserResumoDto();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setNumeroDeTelefone(usuario.getNumeroDeTelefone());
        return dto;

    }

}

package com.api.muinda_kubika.Service.Usuarios;

import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.ProfileApproval.ProfileApprovalResponseDto;
import com.api.muinda_kubika.Enums.ProfileApprovalStatusEnum;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import com.api.muinda_kubika.Exceptions.*;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.Repository.Usuarios.*;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Usuarios.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfileApprovalService {

    private final ProfileApprovalRepository profileApprovalRepository;
    private final DefaultUserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final AdminRepository adminRepository;
    private final AdminInstituicaoRepository adminInstituicaoRepository;
    private final DocenteRepository docenteRepository;
    private final EstudanteRepository estudanteRepository;

    public ProfileApprovalService(
            ProfileApprovalRepository profileApprovalRepository,
            DefaultUserRepository userRepository,
            RolesRepository rolesRepository,
            AdminRepository adminRepository,
            AdminInstituicaoRepository adminInstituicaoRepository,
            DocenteRepository docenteRepository,
            EstudanteRepository estudanteRepository) {

        this.profileApprovalRepository = profileApprovalRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.adminRepository = adminRepository;
        this.adminInstituicaoRepository = adminInstituicaoRepository;
        this.docenteRepository = docenteRepository;
        this.estudanteRepository = estudanteRepository;
    }

    @Transactional
    public ProfileApprovalModel createPendingApproval(
            ProfileTypeEnum profileType, UUID profileId, UUID userId) {

        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        ProfileApprovalModel approval = new ProfileApprovalModel();
        approval.setProfileType(profileType);
        approval.setProfileId(profileId);
        approval.setUsuario(user);
        approval.setStatus(ProfileApprovalStatusEnum.PENDING);
        approval.setIsActive(true);

        return profileApprovalRepository.save(approval);
    }

    public List<ProfileApprovalResponseDto> listPending(ProfileTypeEnum profileType) {
        List<ProfileApprovalModel> approvals = profileType == null
                ? profileApprovalRepository.findByStatus(ProfileApprovalStatusEnum.PENDING)
                : profileApprovalRepository.findByStatusAndProfileType(
                        ProfileApprovalStatusEnum.PENDING, profileType);

        return approvals.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public ProfileApprovalResponseDto getById(UUID approvalId) {
        return mapToDto(findApproval(approvalId));
    }

    public ProfileApprovalResponseDto getByProfile(ProfileTypeEnum profileType, UUID profileId) {
        ProfileApprovalModel approval = profileApprovalRepository
                .findByProfileTypeAndProfileId(profileType, profileId)
                .orElseThrow(() -> new ProfileApprovalNotFoundException(profileId));

        return mapToDto(approval);
    }

    @Transactional
    public String approve(UUID approverId, UUID approvalId) {
        ProfileApprovalModel approval = findApproval(approvalId);
        validatePendingState(approval);
        validateApproverPermission(approval.getProfileType(), approverId);

        DefaultUserModel approver = userRepository.findByIdAndIsActiveTrue(approverId)
                .orElseThrow(() -> new UserNotFoundException(approverId));

        activateProfile(approval.getProfileType(), approval.getProfileId());

        approval.setStatus(ProfileApprovalStatusEnum.ACTIVE);
        approval.setAprovadoPor(approver);
        approval.setDataAprovacao(LocalDateTime.now());
        profileApprovalRepository.save(approval);

        return "Perfil aprovado com sucesso";
    }

    @Transactional
    public String approveByProfile(UUID approverId, ProfileTypeEnum profileType, UUID profileId) {
        ProfileApprovalModel approval = profileApprovalRepository
                .findByProfileTypeAndProfileIdAndStatus(
                        profileType, profileId, ProfileApprovalStatusEnum.PENDING)
                .orElseThrow(() -> new ProfileApprovalNotFoundException(profileId));

        return approve(approverId, approval.getId());
    }

    @Transactional
    public String reject(UUID rejectorId, UUID approvalId, String motivoRejeicao) {
        ProfileApprovalModel approval = findApproval(approvalId);
        validatePendingState(approval);
        validateApproverPermission(approval.getProfileType(), rejectorId);

        DefaultUserModel rejector = userRepository.findByIdAndIsActiveTrue(rejectorId)
                .orElseThrow(() -> new UserNotFoundException(rejectorId));

        approval.setStatus(ProfileApprovalStatusEnum.REJECTED);
        approval.setRejeitadoPor(rejector);
        approval.setDataRejeicao(LocalDateTime.now());
        approval.setMotivoRejeicao(motivoRejeicao);
        profileApprovalRepository.save(approval);

        return "Pedido de perfil rejeitado";
    }

    private ProfileApprovalModel findApproval(UUID approvalId) {
        return profileApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new ProfileApprovalNotFoundException(approvalId));
    }

    private void validatePendingState(ProfileApprovalModel approval) {
        if (approval.getStatus() == ProfileApprovalStatusEnum.ACTIVE) {
            throw new ProfileAlreadyActiveException(approval.getProfileId());
        }
        if (approval.getStatus() == ProfileApprovalStatusEnum.REJECTED) {
            throw new ProfileAlreadyRejectedException(approval.getId());
        }
        if (approval.getStatus() != ProfileApprovalStatusEnum.PENDING) {
            throw new ProfileApprovalInvalidStateException(
                    "Estado de aprovação inválido: " + approval.getStatus());
        }
    }

    private void validateApproverPermission(ProfileTypeEnum profileType, UUID approverId) {
        boolean isAdmin = adminRepository.findByUsuarioIdAndIsActiveTrue(approverId).isPresent();
        boolean isAdminInstituicao = adminInstituicaoRepository.findByUsuarioIdAndIsActiveTrue(approverId).isPresent();

        switch (profileType) {
            case ADMIN, ESTUDANTE -> {
                if (!isAdmin) {
                    throw new InsufficientApprovalPermissionException();
                }
            }
            case ADMIN_INSTITUICAO, DOCENTE -> {
                if (!isAdmin && !isAdminInstituicao) {
                    throw new InsufficientApprovalPermissionException();
                }
            }
        }
    }

    private void activateProfile(ProfileTypeEnum profileType, UUID profileId) {
        switch (profileType) {
            case ADMIN -> activateAdmin(profileId);
            case ADMIN_INSTITUICAO -> activateAdminInstituicao(profileId);
            case DOCENTE -> activateDocente(profileId);
            case ESTUDANTE -> activateEstudante(profileId);
        }
    }

    private void activateAdmin(UUID profileId) {
        AdminModel admin = adminRepository.findById(profileId)
                .orElseThrow(() -> new UserNotFoundException(profileId));

        if (Boolean.TRUE.equals(admin.getIsActive())) {
            throw new ProfileAlreadyActiveException(profileId);
        }

        assignRole(admin.getUsuario(), "ROLE_ADMIN");
        admin.setIsActive(true);
        adminRepository.save(admin);
    }

    private void activateAdminInstituicao(UUID profileId) {
        AdminInstituicaoModel admin = adminInstituicaoRepository.findById(profileId)
                .orElseThrow(() -> new UserNotFoundException(profileId));

        if (Boolean.TRUE.equals(admin.getIsActive())) {
            throw new ProfileAlreadyActiveException(profileId);
        }

        assignRole(admin.getUsuario(), "ROLE_ADMIN_INSTITUICAO");
        admin.setIsActive(true);
        adminInstituicaoRepository.save(admin);
    }

    private void activateDocente(UUID profileId) {
        DocenteModel docente = docenteRepository.findById(profileId)
                .orElseThrow(() -> new UserNotFoundException(profileId));

        if (Boolean.TRUE.equals(docente.getIsActive())) {
            throw new ProfileAlreadyActiveException(profileId);
        }

        assignRole(docente.getUsuario(), "ROLE_DOCENTE");
        docente.setIsActive(true);
        docenteRepository.save(docente);
    }

    private void activateEstudante(UUID profileId) {
        EstudanteModel estudante = estudanteRepository.findById(profileId)
                .orElseThrow(() -> new UserNotFoundException(profileId));

        if (Boolean.TRUE.equals(estudante.getIsActive())) {
            throw new ProfileAlreadyActiveException(profileId);
        }

        assignRole(estudante.getUsuario(), "ROLE_ESTUDANTE");
        estudante.setIsActive(true);
        estudanteRepository.save(estudante);
    }

    private void assignRole(DefaultUserModel user, String roleName) {
        RolesModel role = rolesRepository.findByDescricao(roleName)
                .orElseThrow(RoleNotFoundException::new);

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    private ProfileApprovalResponseDto mapToDto(ProfileApprovalModel approval) {
        ProfileApprovalResponseDto dto = new ProfileApprovalResponseDto();
        dto.setId(approval.getId());
        dto.setProfileType(approval.getProfileType());
        dto.setProfileId(approval.getProfileId());
        dto.setStatus(approval.getStatus());
        dto.setIsActive(approval.getIsActive());
        dto.setCreatedAt(approval.getCreatedAt());
        dto.setUpdatedAt(approval.getUpdatedAt());
        dto.setDataAprovacao(approval.getDataAprovacao());
        dto.setDataRejeicao(approval.getDataRejeicao());
        dto.setMotivoRejeicao(approval.getMotivoRejeicao());

        if (approval.getUsuario() != null) {
            dto.setUsuario(mapToUser(approval.getUsuario()));
        }
        if (approval.getAprovadoPor() != null) {
            dto.setAprovadoPor(mapToUser(approval.getAprovadoPor()));
        }
        if (approval.getRejeitadoPor() != null) {
            dto.setRejeitadoPor(mapToUser(approval.getRejeitadoPor()));
        }

        return dto;
    }

    private DefaultUserResumoDto mapToUser(DefaultUserModel user) {
        DefaultUserResumoDto dto = new DefaultUserResumoDto();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setNumeroDeTelefone(user.getNumeroDeTelefone());
        return dto;
    }
}

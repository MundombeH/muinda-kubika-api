package com.api.muinda_kubika.Service.Usuarios;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.AdminInstituicao.AdminInstituicaoReponseDto;
import com.api.muinda_kubika.DTO.Usuarios.AdminInstituicao.AdminInstituicaoRequestDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import com.api.muinda_kubika.Exceptions.ProfileAlreadyExistsException;
import com.api.muinda_kubika.Exceptions.RoleNotFoundException;
import com.api.muinda_kubika.Exceptions.UserNotFoundException;
import com.api.muinda_kubika.Repository.Instituicoes.InstituicoesRepository;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.Repository.Usuarios.AdminInstituicaoRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Usuarios.AdminInstituicaoModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminInstituicaoService {

    private final AdminInstituicaoRepository adminInstituicaoRepository;
    private final DefaultUserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final InstituicoesRepository instituicoesRepository;
    private final ProfileApprovalService profileApprovalService;

    public AdminInstituicaoService(
            AdminInstituicaoRepository adminInstituicaoRepository,
            DefaultUserRepository userRepository,
            RolesRepository rolesRepository,
            InstituicoesRepository instituicoesRepository,
            ProfileApprovalService profileApprovalService) {

        this.adminInstituicaoRepository = adminInstituicaoRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.instituicoesRepository = instituicoesRepository;
        this.profileApprovalService = profileApprovalService;
    }

    public List<AdminInstituicaoReponseDto> getAllAdminsInstituicao() {
        return adminInstituicaoRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AdminInstituicaoReponseDto getOneAdminInstituicao(UUID userId) {

        AdminInstituicaoModel admin = adminInstituicaoRepository
                .findByUsuarioAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return mapToDto(admin);
    }

    @Transactional
    public String criarPerfilAdminInstituicao(UUID userId, AdminInstituicaoRequestDto dto) {
        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (adminInstituicaoRepository.existsByUsuario(userId)) {
            throw new ProfileAlreadyExistsException(
                    "Já existe um perfil de administrador institucional para este utilizador");
        }

        AdminInstituicaoModel admin = new AdminInstituicaoModel();

        admin.setUsuario(user);
        admin.setIsActive(false);

        if (dto.getInstituicoes() != null &&
                !dto.getInstituicoes().isEmpty()) {
            Set<InstituicaoModel> instituicoes = dto.getInstituicoes()
                    .stream()
                    .map(id -> instituicoesRepository
                            .findByIdAndIsActiveTrue(id)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Instituição não encontrada: " + id)))
                    .collect(Collectors.toSet());

            admin.setInstituicao(instituicoes);
        }

        AdminInstituicaoModel saved = adminInstituicaoRepository.save(admin);
        profileApprovalService.createPendingApproval(
                ProfileTypeEnum.ADMIN_INSTITUICAO, saved.getId(), userId);
        return "Pedido de administrador institucional enviado com sucesso";
    }

    @Transactional
    public String activarPerfilAdmin(UUID activadorId, UUID adminId) {
        return profileApprovalService.approveByProfile(
                activadorId, ProfileTypeEnum.ADMIN_INSTITUICAO, adminId);
    }

    @Transactional
    public void deleteAdmin(UUID adminId) {

        AdminInstituicaoModel admin = adminInstituicaoRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        RolesModel roleAdmin = rolesRepository
                .findByDescricao("ROLE_ADMIN_INSTITUICAO")
                .orElseThrow(RoleNotFoundException::new);

        admin.getUsuario()
                .getRoles()
                .remove(roleAdmin);

        userRepository.save(admin.getUsuario());

        admin.setIsActive(false);

        adminInstituicaoRepository.save(admin);
    }

    private AdminInstituicaoReponseDto mapToDto(
            AdminInstituicaoModel adminModel) {

        AdminInstituicaoReponseDto dto = new AdminInstituicaoReponseDto();

        dto.setId(adminModel.getId());
        dto.setIsActive(adminModel.getIsActive());
        dto.setCreatedAt(adminModel.getCreatedAt());
        dto.setUpdatedAt(adminModel.getUpdatedAt());

        if (adminModel.getUsuario() != null) {
            dto.setUsuario(mapToUser(adminModel.getUsuario()));
        }

        dto.setInstituicoes(adminModel.getInstituicao()
                .stream()
                .map(this::instituicaoDto)
                .collect(Collectors.toSet()));

        return dto;
    }

    private DefaultUserResumoDto mapToUser(DefaultUserModel usuario) {

        DefaultUserResumoDto dto = new DefaultUserResumoDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setNumeroDeTelefone(usuario.getNumeroDeTelefone());

        return dto;
    }

    private InstituicoesResumoDto instituicaoDto(
            InstituicaoModel instituicao) {

        InstituicoesResumoDto dto = new InstituicoesResumoDto();

        dto.setId(instituicao.getId());
        dto.setDescricao(instituicao.getDescricao());
        dto.setTipoInstituicao(instituicao.getTipoInstituicao());
        dto.setNumeroDeTelefone(instituicao.getNumeroDeTelefone());
        dto.setEmail(instituicao.getEmail());
        dto.setIsActive(instituicao.getIsActive());

        return dto;
    }
}
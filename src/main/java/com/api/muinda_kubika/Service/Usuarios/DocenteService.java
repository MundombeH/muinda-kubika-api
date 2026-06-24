package com.api.muinda_kubika.Service.Usuarios;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.Docente.DocenteCriarDto;
import com.api.muinda_kubika.DTO.Usuarios.Docente.DocenteResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.Docente.DocenteRequestDto;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import com.api.muinda_kubika.Exceptions.ProfileAlreadyExistsException;
import com.api.muinda_kubika.Exceptions.RoleNotFoundException;
import com.api.muinda_kubika.Exceptions.UserNotFoundException;
import com.api.muinda_kubika.Repository.Instituicoes.InstituicoesRepository;
import com.api.muinda_kubika.Repository.Localizacao.BairrosRepository;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.Repository.Usuarios.DocenteRepository;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import com.api.muinda_kubika.model.Usuarios.DocenteModel;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class DocenteService {

    private final DocenteRepository docenteRepository;
    private final DefaultUserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final BairrosRepository bairrosRepository;
    private final InstituicoesRepository instituicoesRepository;
    private final ProfileApprovalService profileApprovalService;

    public DocenteService(
            DocenteRepository docenteRepository,
            DefaultUserRepository userRepository,
            RolesRepository rolesRepository,
            BairrosRepository bairrosRepository,
            InstituicoesRepository instituicoesRepository,
            ProfileApprovalService profileApprovalService) {

        this.docenteRepository = docenteRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.bairrosRepository = bairrosRepository;

        this.instituicoesRepository = instituicoesRepository;
        this.profileApprovalService = profileApprovalService;
    }

    public List<DocenteResponseDto> getAllDocente() {
        return docenteRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public DocenteResponseDto getOne(UUID userId) {
        DocenteModel docente =docenteRepository.findByUsuarioIdAndIsActiveTrue(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

        return mapToDto(docente);
    }

    @Transactional
    public String criarPerfilDocente(UUID userId, DocenteCriarDto dto) {

        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

        if (docenteRepository.existsByUsuarioId(userId)) {
            throw new ProfileAlreadyExistsException(userId);
        }

        DocenteModel docente = new DocenteModel();
        docente.setUsuario(user);
        docente.setIdentificacao(dto.getIdentificacao());
        docente.setDepartamento(dto.getDepartamento());
        docente.setGenero(dto.getGenero());
        docente.setIsActive(false);

        if (dto.getInstituicoes() != null && !dto.getInstituicoes().isEmpty()) {
            Set<InstituicaoModel> instituicoes = dto.getInstituicoes()
                .stream()
                .map(id -> instituicoesRepository
                    .findByIdAndIsActiveTrue(id)
                    .orElseThrow(() -> new RuntimeException("Instituição não encontrada: " + id)))
                .collect(Collectors.toSet());
            docente.setInstituicao(instituicoes);
        }

        DocenteModel saved = docenteRepository.save(docente);
        profileApprovalService.createPendingApproval(ProfileTypeEnum.DOCENTE, saved.getId(), userId);

        return "Pedido de perfil docente criado com sucesso";
    }

    @Transactional
    public String activarPerfilDocente(UUID adminId, UUID docenteId) {
        return profileApprovalService.approveByProfile(adminId, ProfileTypeEnum.DOCENTE, docenteId);
    }

    @Transactional
    public DocenteResponseDto updateDocente(DocenteRequestDto dto, UUID userId) {

        DocenteModel docente = docenteRepository.findByUsuarioIdAndIsActiveTrue(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

        BairroModel bairro = bairrosRepository.findByIdAndIsActiveTrue(dto.getBairro())
                        .orElseThrow(() -> new EntityNotFoundException("Bairro não encontrado"));

        docente.setBairro(bairro);
        docente.setIdentificacao(dto.getIdentificacao());
        docente.setGenero(dto.getGenero());
        docente.setDepartamento(dto.getDepartamento());

        if (dto.getInstituicoes() != null && !dto.getInstituicoes().isEmpty()) {

            Set<InstituicaoModel> instituicoes =
                    dto.getInstituicoes()
                            .stream()
                            .map(id -> instituicoesRepository
                                    .findByIdAndIsActiveTrue(id)
                                    .orElseThrow(() ->
                                            new EntityNotFoundException("Instituição não encontrada: " + id)))
                            .collect(Collectors.toSet());

            docente.setInstituicao(instituicoes);
        }
        return mapToDto(docenteRepository.save(docente));
    }

    @Transactional
    public void deleteDocente(UUID userId) {

        DocenteModel docente =docenteRepository.findByUsuarioIdAndIsActiveTrue(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

        RolesModel role = rolesRepository.findByDescricao("ROLE_DOCENTE")
                        .orElseThrow(RoleNotFoundException::new);

        docente.getUsuario().getRoles().remove(role);
        userRepository.save(docente.getUsuario());

        docente.setIsActive(false);
        docenteRepository.save(docente);
    }

    private DocenteResponseDto mapToDto(DocenteModel docente) {

        DocenteResponseDto dto = new DocenteResponseDto();

        dto.setId(docente.getId());
        dto.setIdentificacao(docente.getIdentificacao());
        dto.setGenero(docente.getGenero());
        dto.setIsActive(docente.getIsActive());
        dto.setCreatedAt(docente.getCreatedAt());
        dto.setUpdatedAt(docente.getUpdatedAt());

        if (docente.getUsuario() != null) {
            dto.setUsuario(userToDto(docente.getUsuario()));
        }

        if (docente.getInstituicao() != null) {
            dto.setInstituicoes(
                    docente.getInstituicao()
                            .stream()
                            .map(this::instituicaoDto)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    private InstituicoesResumoDto instituicaoDto(InstituicaoModel instituicaoModel) {
        InstituicoesResumoDto dto = new InstituicoesResumoDto();
        dto.setTipoInstituicao(instituicaoModel.getTipoInstituicao());
        dto.setDescricao(instituicaoModel.getDescricao());
        dto.setIsActive(instituicaoModel.getIsActive());
        dto.setNumeroDeTelefone(instituicaoModel.getNumeroDeTelefone());

        return dto;

    }

    private DefaultUserResumoDto userToDto(DefaultUserModel usuario) {
        DefaultUserResumoDto dto = new DefaultUserResumoDto();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setId(usuario.getId());
        dto.setNumeroDeTelefone(usuario.getNumeroDeTelefone());
        return  dto;

    }
}
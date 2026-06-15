package com.api.muinda_kubika.Service.Usuarios;

import com.api.muinda_kubika.DTO.Localizacao.BairroResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.Estudante.EstudanteRequestDto;
import com.api.muinda_kubika.DTO.Usuarios.Estudante.EstudanteResponseDto;
import com.api.muinda_kubika.Exceptions.ProfileAlreadyExistsException;
import com.api.muinda_kubika.Exceptions.RoleNotFoundException;
import com.api.muinda_kubika.Exceptions.UserNotFoundException;
import com.api.muinda_kubika.Repository.Localizacao.BairrosRepository;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.Repository.Usuarios.AdminRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.Repository.Usuarios.EstudanteRepository;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import com.api.muinda_kubika.model.Usuarios.EstudanteModel;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final DefaultUserRepository userRepository;
    private final BairrosRepository bairrosRepository;
    private final RolesRepository rolesRepository;
    private final AdminRepository adminRepository;

    public EstudanteService(
        EstudanteRepository estudanteRepository,
        DefaultUserRepository userRepository,
        BairrosRepository bairrosRepository,
        RolesRepository rolesRepository,
        AdminRepository adminRepository
    ) {
        this.estudanteRepository = estudanteRepository;
        this.userRepository = userRepository;
        this.bairrosRepository = bairrosRepository;
        this.rolesRepository = rolesRepository;
        this.adminRepository = adminRepository;
    }

    public List<EstudanteResponseDto> getAllEstudantes() {
        return estudanteRepository
            .findByIsActiveTrue()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public EstudanteResponseDto getOne(UUID id) {
        EstudanteModel estudante = estudanteRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Estudante não encontrado!")
            );
        return mapToDto(estudante);
    }

    @Transactional
    public EstudanteResponseDto UpdateEstudante(
        EstudanteRequestDto dto,
        UUID userId
    ) {
        EstudanteModel estudante = estudanteRepository
            .findByUsuarioAndIsActiveTrue(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        BairroModel bairro = bairrosRepository
            .findByIdAndIsActiveTrue(dto.getBairro())
            .orElseThrow(() ->
                new EntityNotFoundException("Bairro ano encontrado")
            );
        estudante.setAno(dto.getAno());
        estudante.setCurso(dto.getCurso());
        estudante.setBairro(bairro);
        estudante.setIdentificacao(dto.getIdentificacao());
        estudante.setIdentificacao(dto.getIdentificacao());
        estudante.setGenero(dto.getGenero());

        EstudanteModel save = estudanteRepository.save(estudante);
        return mapToDto(save);
    }

    @Transactional
    public String criarPerfilEstudante(UUID userId) {
        DefaultUserModel usuario = userRepository
            .findByIdAndIsActiveTrue(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        if (estudanteRepository.existsByUsuario(userId)) {
            throw new ProfileAlreadyExistsException(userId);
        }

        EstudanteModel estudante = new EstudanteModel();
        estudante.setUsuario(usuario);
        estudante.setIsActive(false);

        estudanteRepository.save(estudante);

        return "Perfil estudante criado com sucesso. Aguardando ativação.";
    }

    @Transactional
    public String activarPerfilEstudante(UUID adminId, UUID estudanteId) {
        EstudanteModel estudante = estudanteRepository
            .findById(estudanteId)
            .orElseThrow(() -> new UserNotFoundException(estudanteId));

        boolean isAdmin = adminRepository
            .findByUsuarioAndIsActiveTrue(adminId)
            .isPresent();

        if (!isAdmin) {
            return "Usuário não tem permissões para efetuar esta operação";
        }

        if (estudante.getIsActive()) {
            throw new ProfileAlreadyExistsException(estudanteId);
        }

        RolesModel roleEstudante = rolesRepository
            .findByDescricao("ROLE_ESTUDANTE")
            .orElseThrow(RoleNotFoundException::new);

        DefaultUserModel user = estudante.getUsuario();

        user.getRoles().add(roleEstudante);
        userRepository.save(user);

        estudante.setIsActive(true);
        estudanteRepository.save(estudante);

        return "Perfil estudante ativado com sucesso";
    }

    public void deleteEstudante(UUID userId) {
        EstudanteModel estudante = estudanteRepository
            .findByUsuarioAndIsActiveTrue(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        estudante.setIsActive(false);
        estudanteRepository.save(estudante);
    }

    private EstudanteResponseDto mapToDto(EstudanteModel model) {
        EstudanteResponseDto dto = new EstudanteResponseDto();

        dto.setId(model.getId());
        dto.setAno(model.getAno());
        dto.setIdentificacao(model.getIdentificacao());
        dto.setCurso(model.getCurso());
        dto.setIsActive(model.getIsActive());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setGenero(model.getGenero());

        if (model.getBairro() != null) {
            dto.setBairro(mapToBairro(model.getBairro()));
        }

        if (model.getBairro() != null) {
            dto.setUsuario(mapToUsuario(model.getUsuario()));
        }
        return dto;
    }

    private BairroResumoDto mapToBairro(BairroModel bairro) {
        BairroResumoDto dto = new BairroResumoDto();
        dto.setDescricao(bairro.getDescricao());
        dto.setId(bairro.getId());
        dto.setIsActive(bairro.getIsActive());
        dto.setMunicipioId(bairro.getMunicipio().getId());
        return dto;
    }

    private DefaultUserResumoDto mapToUsuario(DefaultUserModel usuario) {
        DefaultUserResumoDto dto = new DefaultUserResumoDto();

        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setNumeroDeTelefone(usuario.getNumeroDeTelefone());

        return dto;
    }
}

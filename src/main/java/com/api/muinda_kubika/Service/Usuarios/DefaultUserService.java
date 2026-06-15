package com.api.muinda_kubika.Service.Usuarios;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.api.muinda_kubika.DTO.Roles_Permissions.PermissionResumoDto;
import com.api.muinda_kubika.model.Roles_permissions.PermissionsModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.api.muinda_kubika.DTO.Roles_Permissions.RolesResumoDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserRequestDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.UserUpdatePasswordDto;
import com.api.muinda_kubika.Exceptions.EmailAlreadyExistException;
import com.api.muinda_kubika.Exceptions.RoleNotFoundException;
import com.api.muinda_kubika.Exceptions.TelefoneAlreadyExistException;
import com.api.muinda_kubika.Exceptions.UserNotFoundException;
import com.api.muinda_kubika.Repository.Roles_Permissions.RolesRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import jakarta.transaction.Transactional;

@Service
public class DefaultUserService {
    private final DefaultUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public DefaultUserService(DefaultUserRepository userRepository, PasswordEncoder passwordEncoder,
            RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    public List<DefaultUserResponseDto> getAllUsers() {
        return userRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    public DefaultUserResponseDto getOneUser(UUID id) {
        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToService(user);
    }

    public void deleteUser(UUID id) {
        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setIsActive(false);
        userRepository.save(user);

    }

    @Transactional
    public DefaultUserResponseDto createUser(DefaultUserRequestDto dto) {
        DefaultUserModel user = new DefaultUserModel();

        user.setNumeroDeTelefone(dto.getNumeroDeTelefone());

        if (userRepository.existsByEmailAndIsActiveTrue(dto.getEmail())) {
            throw new EmailAlreadyExistException(dto.getEmail());
        }

        if (userRepository.existsByNumeroDeTelefoneAndIsActiveTrue(dto.getNumeroDeTelefone())) {
            throw new TelefoneAlreadyExistException(dto.getNumeroDeTelefone());
        }
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setDataDeNascimento((dto.getDataDeNascimento()));

        // encriptar password
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Roles
        RolesModel role = rolesRepository.findByDescricao("ROLE_USUARIO")
                .orElseThrow(RoleNotFoundException::new);

        user.setRoles(Set.of(role));

        DefaultUserModel saved = userRepository.save(user);

        return mapToService(saved);
    }

    @Transactional
    public DefaultUserResponseDto updateUser(UUID id, DefaultUserRequestDto dto) {
        // 1. Busca o usuario existente ou lança erro
        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Validações de Unicidade (Apenas se o Email mudaram)

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmailAndIsActiveTrue(dto.getEmail())) {
            throw new EmailAlreadyExistException(dto.getEmail());
        }
        if (!user.getNumeroDeTelefone().equals(dto.getNumeroDeTelefone())
                && userRepository.existsByNumeroDeTelefoneAndIsActiveTrue(dto.getNumeroDeTelefone())) {
            throw new TelefoneAlreadyExistException(dto.getNumeroDeTelefone());
        }

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setNumeroDeTelefone(dto.getNumeroDeTelefone());
        user.setDataDeNascimento(dto.getDataDeNascimento());

        DefaultUserModel updated = userRepository.save(user);

        return mapToService(updated);
    }

    public String updatePassword(UUID userId, UserUpdatePasswordDto dto) {
        DefaultUserModel user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        System.out.println("Nova senha:" + dto.getNewPassword());
        return "Senha atualizada com sucesso";
    }

    public DefaultUserResponseDto mapToService(DefaultUserModel user) {
        DefaultUserResponseDto dto = new DefaultUserResponseDto();

        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setNumeroDeTelefone(user.getNumeroDeTelefone());
        dto.setDataDeNascimento(user.getDataDeNascimento());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setIsActive(user.getIsActive());

        Set<RolesResumoDto> roles = user.getRoles()
                .stream()
                .filter(RolesModel::getIsActive)
                .map(this::roleToDto)
                .collect(Collectors.toSet());

        dto.setRoles(roles);
        return dto;
    }

    // Auxiliar para converter Role Model para Role DTO
    private RolesResumoDto roleToDto(RolesModel role) {
        RolesResumoDto dto = new RolesResumoDto();
        dto.setDescricao(role.getDescricao());
        dto.setId(role.getId());
        dto.setIsActive(role.getIsActive());

        Set<PermissionResumoDto> permissions = role.getPermissions()
                .stream()
                .filter(PermissionsModel::getIsActive)
                .map(this::permissionToDto)
                .collect(Collectors.toSet());
        return dto;
    }

    private PermissionResumoDto permissionToDto(PermissionsModel permissions) {
        PermissionResumoDto dto = new PermissionResumoDto();

        dto.setDescricao(permissions.getDescricao());
        dto.setId(permissions.getId());
        dto.setIsActive(permissions.getIsActive());

        return dto;
    }

}

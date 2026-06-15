package com.api.muinda_kubika.Service.Security;

import com.api.muinda_kubika.DTO.Auth.LoginRequestDto;
import com.api.muinda_kubika.DTO.Auth.LoginResponseDto;
import com.api.muinda_kubika.Exceptions.InvalidCredentialsException;
import com.api.muinda_kubika.Exceptions.InvalidRefreshTokenException;
import com.api.muinda_kubika.Repository.Security.RefreshTokenRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.model.Roles_permissions.PermissionsModel;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import com.api.muinda_kubika.model.Security.RefreshTokenModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import com.api.muinda_kubika.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DefaultUserRepository defaultUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
        DefaultUserRepository defaultUserRepository,
        RefreshTokenRepository refreshTokenRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.defaultUserRepository = defaultUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto)
        throws InvalidCredentialsException {
        DefaultUserModel user = defaultUserRepository
            .findByNumeroDeTelefoneOrEmail(
                dto.getIdentificador(),
                dto.getIdentificador()
            )
            .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
            .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Set<String> roles = extractRoles(user);
        Set<String> permissions = extractPermissions(user);

        String accessToken = jwtService.generateToken(
            user.getId(),
            roles,
            permissions
        );
        String refreshToken = createRefreshToken(user);

        return buildLoginResponse(
            user.getId(),
            roles,
            permissions,
            accessToken,
            refreshToken
        );
    }

    @Transactional
    public LoginResponseDto refresh(String rawRefreshToken) {
        RefreshTokenModel storedToken = validateRefreshToken(rawRefreshToken);
        DefaultUserModel user = storedToken.getUser();

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            revokeRefreshToken(storedToken);
            throw new InvalidRefreshTokenException();
        }

        revokeRefreshToken(storedToken);

        Set<String> roles = extractRoles(user);
        Set<String> permissions = extractPermissions(user);

        String accessToken = jwtService.generateToken(
            user.getId(),
            roles,
            permissions
        );
        String newRefreshToken = createRefreshToken(user);

        return buildLoginResponse(
            user.getId(),
            roles,
            permissions,
            accessToken,
            newRefreshToken
        );
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        String tokenHash = hashToken(rawRefreshToken);
        refreshTokenRepository
            .findByTokenHashAndIsActiveTrue(tokenHash)
            .ifPresent(this::revokeRefreshToken);
    }

    private LoginResponseDto buildLoginResponse(
        java.util.UUID userId,
        Set<String> roles,
        Set<String> permissions,
        String accessToken,
        String refreshToken
    ) {
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(accessToken);
        responseDto.setRefreshToken(refreshToken);
        responseDto.setExpiresInMs(jwtService.getExpirationMs());
        responseDto.setRefreshExpiresInMs(jwtService.getRefreshExpirationMs());
        responseDto.setUserId(userId);
        responseDto.setRoles(roles);
        responseDto.setPermissions(permissions);
        return responseDto;
    }

    private Set<String> extractRoles(DefaultUserModel user) {
        return user
            .getRoles()
            .stream()
            .filter(role -> Boolean.TRUE.equals(role.getIsActive()))
            .map(RolesModel::getDescricao)
            .collect(Collectors.toSet());
    }

    private Set<String> extractPermissions(DefaultUserModel user) {
        return user
            .getRoles()
            .stream()
            .filter(role -> Boolean.TRUE.equals(role.getIsActive()))
            .flatMap(role -> role.getPermissions().stream())
            .filter(permission -> Boolean.TRUE.equals(permission.getIsActive()))
            .map(PermissionsModel::getDescricao)
            .collect(Collectors.toSet());
    }

    private String createRefreshToken(DefaultUserModel user) {
        String rawToken = generateRawToken();

        RefreshTokenModel refreshToken = new RefreshTokenModel();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setExpiresAt(
            LocalDateTime.now().plusSeconds(
                jwtService.getRefreshExpirationMs() / 1000
            )
        );
        refreshToken.setRevokedAt(null);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    private RefreshTokenModel validateRefreshToken(String rawRefreshToken) {
        String tokenHash = hashToken(rawRefreshToken);

        return refreshTokenRepository
            .findByTokenHashAndIsActiveTrue(tokenHash)
            .filter(token -> token.getRevokedAt() == null)
            .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
            .orElseThrow(InvalidRefreshTokenException::new);
    }

    private void revokeRefreshToken(RefreshTokenModel token) {
        token.setIsActive(false);
        token.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(token);
    }

    private String generateRawToken() {
        byte[] random = new byte[64];
        new java.security.SecureRandom().nextBytes(random);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(random);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                rawToken.getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                "SHA-256 não disponível no ambiente",
                ex
            );
        }
    }
}

package com.api.muinda_kubika.Repository.Security;

import com.api.muinda_kubika.model.Security.RefreshTokenModel;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository
    extends JpaRepository<RefreshTokenModel, UUID> {

    Optional<RefreshTokenModel> findByTokenHashAndIsActiveTrue(String tokenHash);

    long countByUserIdAndIsActiveTrueAndRevokedAtIsNullAndExpiresAtAfter(
        UUID userId,
        LocalDateTime now
    );
}

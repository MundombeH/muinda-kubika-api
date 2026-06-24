package com.api.muinda_kubika.Repository.Usuarios;

import com.api.muinda_kubika.model.Usuarios.AdminInstituicaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminInstituicaoRepository extends JpaRepository<AdminInstituicaoModel, UUID> {
    Optional<AdminInstituicaoModel> findByIdAndIsActiveTrue(UUID uuid);
    List<AdminInstituicaoModel> findByIsActiveTrue();
    Optional<AdminInstituicaoModel> findByUsuarioIdAndIsActiveTrue(UUID userId);

    boolean existsByUsuarioId(UUID userId);
}

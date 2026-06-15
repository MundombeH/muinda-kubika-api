package com.api.muinda_kubika.Repository.Usuarios;

import com.api.muinda_kubika.model.Usuarios.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, UUID> {
    List<AdminModel> findByIsActiveTrue();
    Optional<AdminModel> findByUsuarioAndIsActiveTrue(UUID userId);

    Boolean existsByUsuarioId(UUID userId);
}

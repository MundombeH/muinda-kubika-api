package com.api.muinda_kubika.Repository.Usuarios;

import com.api.muinda_kubika.model.Usuarios.EstudanteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstudanteRepository extends JpaRepository<EstudanteModel, UUID> {

    Optional<EstudanteModel> findByIdAndIsActiveTrue(UUID uuid);

    List<EstudanteModel> findByIsActiveTrue();
    Optional<EstudanteModel> findByUsuarioAndIsActiveTrue(UUID userId);

    boolean existsByUsuario(UUID userId);
}

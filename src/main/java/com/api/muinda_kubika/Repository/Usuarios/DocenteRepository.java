package com.api.muinda_kubika.Repository.Usuarios;

import com.api.muinda_kubika.model.Usuarios.DocenteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteModel, UUID> {
    Optional<DocenteModel> findByIdAndIsActiveTrue(UUID uuid);
    List<DocenteModel> findByIsActiveTrue();
    Optional<DocenteModel> findByUsuarioAndIsActiveTrue(UUID userId);

    boolean existsByUsuario(UUID userId);
}

package com.api.muinda_kubika.Repository.Files;

import com.api.muinda_kubika.model.Files.RepositorioModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioRepository
    extends JpaRepository<RepositorioModel, UUID> {
    Optional<RepositorioModel> findByDocumentoId(UUID documentoId);
}

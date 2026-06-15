package com.api.muinda_kubika.Repository.Files;

import com.api.muinda_kubika.model.Files.DocumentoAnaliseModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoAnaliseRepository
        extends JpaRepository<DocumentoAnaliseModel, UUID> {
    Optional<DocumentoAnaliseModel> findByDocumentoId(UUID documentoId);
}

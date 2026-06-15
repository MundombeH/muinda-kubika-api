package com.api.muinda_kubika.Repository.Files;

import com.api.muinda_kubika.model.Files.DocumentosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentosModel, UUID> {
    List<DocumentosModel> findByIsActiveTrue();

    Optional<DocumentosModel> findByIdAndIsActiveTrue(UUID uuid);
}

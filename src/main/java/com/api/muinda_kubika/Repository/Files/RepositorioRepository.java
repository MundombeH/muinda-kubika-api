package com.api.muinda_kubika.Repository.Files;

import com.api.muinda_kubika.model.Files.RepositorioModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioRepository
    extends JpaRepository<RepositorioModel, UUID> {
    Optional<RepositorioModel> findByDocumentoId(UUID documentoId);
    @Query("SELECT r FROM RepositorioModel r JOIN FETCH r.documento d WHERE d.id IN :ids")
    List<RepositorioModel> findByDocumentoIdIn(@Param("ids") List<UUID> ids);
}

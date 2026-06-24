package com.api.muinda_kubika.Repository.Categorias_Tags;

import com.api.muinda_kubika.model.Categorias_Tags.CategoriasModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriasRepository extends JpaRepository<CategoriasModel, UUID> {
    List<CategoriasModel> findByIsActiveTrue();
    Optional<CategoriasModel> findByIdAndIsActiveTrue(UUID id);
    Optional<CategoriasModel> findByDescricaoAndIsActiveTrue(String descricao);
}

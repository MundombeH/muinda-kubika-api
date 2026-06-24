package com.api.muinda_kubika.Repository.Categorias_Tags;

import com.api.muinda_kubika.model.Categorias_Tags.TagsModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<TagsModel, UUID> {
    List<TagsModel> findByIsActiveTrue();
    Optional<TagsModel> findByIdAndIsActiveTrue(UUID id);
    Optional<TagsModel> findByDescricaoAndIsActiveTrue(String descricao);
}

package com.api.muinda_kubika.Repository.Localizacao;

import java.util.List;
import java.util.UUID;
import com.api.muinda_kubika.model.Localizacao.PaisModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface PaisesRepository  extends JpaRepository<PaisModel, UUID>{

    Optional<PaisModel> findByIdAndIsActiveTrue(UUID id);
    Optional<PaisModel> findByDescricaoAndIsActiveTrue(String descricao);

    Optional<PaisModel> existsByDescricao(String descricao);

    List<PaisModel> findByIsActiveTrue();
}
